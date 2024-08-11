package com.example.demo.reports;

import com.example.demo.bean.AllP;
import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.InUtilsR;
import com.example.demo.repository.*;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.*;

public class SummaryReport implements ReportI {
    private InUtilsR inu;
    private HashMap<Integer, Integer> lmap = null;
    private HashMap<Integer, Integer> cmap = null;
    private HashMap<Integer, Integer> inmap = null;

    private LedgerRepository lrepo = null;
    private AllP allData;
    private GscatRepository grepo = null;
    private LmapRepository lmapr = null;
    private CmapRepository cmapr = null;
    private InmapRepository inmapr = null;

    public SummaryReport(Repos r) {
        this.lrepo = r.getLedgerRepository();
        this.lmap = new HashMap<Integer,Integer>();
        this.cmap = new HashMap<Integer,Integer>();
        this.inmap = new HashMap<Integer,Integer>();

        this.grepo = r.getGscat();
        this.lmapr = r.getLmap();
        this.cmapr = r.getCmap();
        this.inmapr = r.getInmap();
        this.inu = new InUtilsR(this.grepo);
        initMaps();
    }

    private void initMaps() {
        List<Lmap> lm = lmapr.findAll();
        for (Lmap l : lm) {
            lmap.put(l.getLid(), l.getGid());
        }
        List<Cmap> cm = cmapr.findAll();
        for (Cmap c : cm) {
            cmap.put(c.getCid(), c.getGid());
        }
        List<Inmap> inm = inmapr.findAll();
        for (Inmap c : inm) {
            inmap.put(c.getLid(), c.getGid());
        }
    }

    public SummaryReport(LedgerRepository lrepo) {
        this.lrepo = lrepo;
    }
    public AllP getAllData() {
        return this.allData;
    }
    public String go(FileWriter w, SessionDTO session) throws Exception {

        this.allData = new AllP();
        LData ld = new LData(lrepo);
        List<Ledger> data = ld.filterByDate(session, null, null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        w.write("Start : " + dates.getStart().toString() + " Stop: " + dates.getStop().toString() + "\n");
        HashMap<String, Catsort> inm = doIn(inmap, data);

        List<HashMap<String, Catsort>> outm = doOut(w, session,data, dates);
        HashMap<String, Catsort> outp = new HashMap<String, Catsort>();

        double totalIn = 0;
        Set<String> keys = inm.keySet();
        for (String key : keys) {
            Catsort value = inm.get(key);
            totalIn += value.getAmount();
        }

        double totalOut = 0;
        keys = outm.get(0).keySet();
        for (String key : keys) {
            Catsort value = outm.get(0).get(key);

            totalOut += value.getAmount();
        }

        for (String key : keys) {
            Catsort nv = new Catsort();
            nv.reverse();
            nv.setAmount(Utils.convertDouble((outm.get(0).get(key).getAmount() / totalOut) * 100));
            nv.setLabel(outm.get(0).get(key).getLabel());
            outp.put(key, nv);
        }

        totalIn = Utils.convertDouble(totalIn);
        totalOut = Utils.convertDouble(totalOut);

        double net = Utils.convertDouble(totalIn + totalOut);
        w.write("IN: " + totalIn + " Out: " + totalOut + " Net: " + net);

        p("In",w,inm);
        w.write("\n");

        HashMap<String, Catsort> m = outm.get(0);
        Catsort misc = m.get("Misc");
        if (misc != null) {
            Catsort c = m.get("Checks");
            if (c != null){
                misc.setAmount(Utils.convertDouble(misc.getAmount() + c.getAmount()));
                m.remove("Checks");
            }
        }

        p("Summary",w,m);
        p("Checks", w, outm.get(1));
        p("Misc",w,outm.get(2));

        p("Percent",w,outp);

        return null;
    }

    private HashMap<String, Catsort> doIn(HashMap<Integer,Integer> inmap, List<Ledger> data) throws Exception {
        List<Ledger> inl = new ArrayList<Ledger>();

        for (Ledger l : data) {
            if (l.getAmount() > 0)
                inl.add(l);
        }
        HashMap<String, Catsort> inm = new HashMap<String, Catsort>();
        for (Ledger l : inl) {
            String n = null;

            Integer I = inmap.get(l.getLabel().getId());
            if (I != null) {
                if (grepo == null) {
                    System.out.println("NO GREPO!");
                } else {
                    Optional<Gscat> m = grepo.findById(I);
                    if (m.isPresent()) {
                        Gscat g = m.get();
                        n = g.getName();
                    }
                }
            }
            if (n == null) {
                n = "MiscIn";
            }
            Catsort r = inu.putm(inm,n,l.getAmount());
            r.reverse();
        }
        return inm;
    }


    private  List<HashMap<String, Catsort>> doOut(FileWriter w, SessionDTO session, List<Ledger> data, StartStop dates) throws Exception {
        List<HashMap<String, Catsort>> ret = new ArrayList<HashMap<String,Catsort>>();
        List<Ledger> out = new ArrayList<Ledger>();

        for (Ledger l : data) {
            if (l.getAmount() < 0)
                out.add(l);
        }

        List<Ledger> checks = new ArrayList<Ledger>();
        List<Ledger> death = new ArrayList<Ledger>();
        HashMap<String, Catsort> outm = new HashMap<String, Catsort>();
        HashMap<String, Catsort> miscm = new HashMap<String, Catsort>();
        for (Ledger l : out) {
            if (l.getStype().getId() == 8)
                continue;
            if (l.getLabel().getId() == 10312) {
                checks.add(l);
                continue;
            }
            String n = null;
            if ((l.getStype().getId() == 3) || (l.getStype().getId() == 4) || (l.getStype().getId() == 6)) {
                n = l.getStype().getName();
            } else {
                Integer I = lmap.get(l.getLabel().getId());
                if (I != null) {
                    Optional<Gscat> m = grepo.findById(I);
                    if (m.isPresent()) {
                        Gscat g = m.get();
                        n = g.getName();
                    }
                } else {
                    //n = l.getLabel().getName();
                }
            }

            if (n == null) {
                n = "Misc";
                inu.putm(miscm,l.getLabel().getName(),l.getAmount());
            } else {
                death.add(l);
            }
            inu.putm(outm,n,l.getAmount());
        }
        out.removeAll(death);

        HashMap<String, Catsort> checksm = new HashMap<String, Catsort>();

        for (Ledger l : checks) {
            Payee p = l.getChecks().getPayee();
            if (p.getCheckType().getId() == 6)
                continue;
            String n = null;
            if (p.getCheckType().getId() == 3) {
                n =  "Annual";
            } else {
                Integer I = cmap.get(l.getChecks().getPayee().getId());
                if (I != null) {
                    Optional<Gscat> m = grepo.findById(I);
                    if (m.isPresent()) {
                        Gscat g = m.get();
                        n = g.getName();
                    }
                } else {
                    inu.putm(checksm, l.getChecks().getPayee().getName(), l.getAmount());
                    n = "Checks";
                }
            }
            inu.putm(outm,n, l.getAmount());
        }
        ret.add(outm);
        ret.add(checksm);
        ret.add(miscm);
        return ret;
    }

    private void p(String label, FileWriter w, HashMap<String, Catsort> map)  throws Exception {
        double total = 0;
        for (Catsort c : map.values()) {
            total += c.getAmount();
        }
        total = Utils.convertDouble(total);
        w.write("\n" + label + " " + total + "\n");
        List<Catsort> lc = new ArrayList<Catsort>(map.values());

        Collections.sort(lc);
        for (Catsort c : lc) {
            w.write(c.getLabel() + " " + c.getAmount() + "\n");
        }
    }


}
