package com.example.demo.actions;
import com.example.demo.bean.Catsort;
import com.example.demo.domain.*;
import com.example.demo.bean.StartStop;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.GscatRepository;
import com.example.demo.repository.InmapRepository;
import com.example.demo.repository.IntableRepository;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.reports.utils.InUtilsR;
import com.example.demo.utils.Utils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InAction extends BaseAction implements ActionI{
    private HashMap<Integer,Integer> inmap = null;
    private final InmapRepository inmapr;
    private boolean isNew;

    public InAction(Repos r) {
        super(r);

        this.isNew = false;
        inmapr = repos.getInmap();

        initMaps();
    }

    public boolean go(SessionDTO session) throws Exception {
        this.isNew = true;
        LocalDate start = session.getStart();
        LocalDate stop = session.getStop();

        PayperiodRepository prepo = repos.getPayPeriod();
        List<Payperiod> pps = prepo.findAllByStartBetweenOrderByStartAsc(start,stop);
        if ((pps == null) || pps.isEmpty()) {
            System.out.println("Could not find " + start.toString() + " " + stop.toString());
            return false;
        }
        Consolidate c = session.getConsolidate();
        boolean h = ((c != null) && (c.equals(Consolidate.HALF)));

        Payperiod p;
        if (!h) {
            p = pps.get(0);
        } else {
            if (pps.size() == 1) {
                System.out.println("No Payperiod on half.");
                return false;
            }
            p = pps.get(1);
        }
        performAction(p);
        return true;
    }

    public List<TLedger> getData(StartStop dates) {
        List<TLedger> data = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(dates.getStart(), dates.getStop());
        List<TLedger> ret = new ArrayList<>();
        for (TLedger l : data) {
            if ((l.getLtype().getId() == 3) ||
                    (l.getLtype().getId() == 11) ||
                    (l.getLtype().getId() == 12) ||
                    (l.getLtype().getId() == 14)) {
                if ((l.getAmount() > 0) && (l.getLabel().getCategory().getId() != 28)) {
                    ret.add(l);
                }
            }
            if ((l.getLabel().getId() == 13149) ||
                    (l.getLabel().getId() == 10288) ||
                    (l.getLabel().getId() == 10428)) {
                ret.add(l);
            }
        }
        return ret;
    }
    public void performAction(Payperiod p) {

        StartStop dates = new StartStop();
        dates.setStart(p.getStart());
        dates.setStop(p.getStop());

        InUtilsR obj = new InUtilsR(repos.getGscat());

        List<TLedger> data = getData(dates);
        double total;
        try {
            HashMap<String, Catsort> m = obj.doIn(inmap, data);

            total = p(m);
            Intable iobj;
            if (this.isNew) {
                iobj = new Intable();
                try {
                    IntableRepository r = repos.getIntable();
                    r.saveAndFlush(iobj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                p.setIna(iobj);
            } else {
                iobj = p.getIna();
            }
            setIn(iobj,m,total);
            if (!isNew) {
                PayperiodRepository rp = repos.getPayPeriod();
                rp.saveAndFlush(p);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private double getData(HashMap<String, Catsort> data, String key) {
        Catsort value = data.get(key);
        return (value != null) ? value.getAmount() : 0;
    }

    private void doSet(Intable dobj, HashMap<String,Catsort> data) {
        GscatRepository gcatR = repos.getGscat();
        List<Gscat> inc = gcatR.findAllByCtype(0);
        Method[] methods = dobj.getClass().getMethods();

        for (Gscat obj : inc) {
            String name = obj.getName();
            double d = getData(data,name);
            String mname = "set".concat(name);
            for (int i = 0;i<methods.length;i++) {
                if (methods[i].getName().equals(mname)) {
                    try {
                        methods[i].invoke(dobj, d);
                    } catch (Exception ex) {
                        System.out.println("*** " + mname + " *** " + d);
                        ex.printStackTrace();
                    }
                }
            }
        }
        /*
        obj.setWork(getData(data,"Work"));
        obj.setMlsale(getData(data,"MLSale"));
        obj.setMldividend(getData(data,"MLDividend"));
        obj.setMlin(getData(data,"MLIn"));
        obj.setArizona(getData(data,"Arizona"));
        obj.setMiscin(getData(data,"Misc"));
        obj.setInterest(getData(data,"Interest"));
        obj.setRefund(getData(data,"Refund"));
        */
    }

    private void setIn(Intable obj, HashMap<String,Catsort> data, double total) {

        obj.setTotal(Utils.convertDouble(total));

        doSet(obj,data);

        try {
            IntableRepository r = repos.getIntable();
            r.saveAndFlush(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private double p(HashMap<String, Catsort> map)  {
        double total = 0;
        for (Catsort c : map.values()) {
            if (!c.getLabel().equals("Creditfree")) {
                total += c.getAmount();
            }
        }
        total = Utils.convertDouble(total);
        List<Catsort> lc = new ArrayList<>(map.values());

        Collections.sort(lc);
        for (Catsort c : lc) {
            System.out.println(c.getLabel() + " " + c.getAmount());
        }
        return total;
    }

    private void initMaps() {
        List<Inmap> inm = inmapr.findAll();
        inmap = new HashMap<>();
        for (Inmap c : inm) {
            inmap.put(c.getLid(), c.getGid());
        }
    }
}
