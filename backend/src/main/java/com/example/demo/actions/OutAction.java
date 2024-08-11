package com.example.demo.actions;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Outtable;
import com.example.demo.domain.Payperiod;
import com.example.demo.domain.TLedger;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.OuttableRepository;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.Utils;

import java.time.LocalDate;
import java.util.List;

public class OutAction extends BaseAction implements ActionI {
    private boolean isNew;

    public OutAction(Repos r) {
        super(r);

        this.isNew = false;
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

    public double  getFilterData(StartStop dates) {
        double ret = 0;
        List<TLedger> data = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(dates.getStart(), dates.getStop());

        for (TLedger l : data) {
            if ((l.getLtype().getId() == 3) ||
                    (l.getLtype().getId() == 5) ||
                    (l.getLtype().getId() == 6) ||
                    (l.getLtype().getId() == 11) ||
                    (l.getLtype().getId() == 12) ||
                    (l.getLtype().getId() == 14)) {
                if ((l.getAmount() < 0)  && (l.getLabel().getCategory().getId() != 28)){
                    ret += l.getAmount();
                }
            }

        }
        return Utils.convertDouble(ret);
    }

    public double getCreditData(StartStop dates) {
        List<TLedger> data = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(dates.getStart(), dates.getStop());
        double ret = 0;
        for (TLedger l : data) {
            if ((l.getLtype().getId() == 7) ||
                    (l.getLtype().getId() == 8) ||
                    (l.getLtype().getId() == 9) ||
                    (l.getLtype().getId() == 10)) {
                if (l.getAmount() < 0) {
                    ret += l.getAmount();
                }
            }

        }
        return Utils.convertDouble(ret);
    }



    public void performAction(Payperiod p) {
        StartStop dates = new StartStop();
        dates.setStart(p.getStart());
        dates.setStop(p.getStop());

        double outr  = getFilterData(dates);
        double outc  = getCreditData(dates);

        try {
            Outtable oobj;
            if (this.isNew) {
                oobj = new Outtable();
                try {
                    OuttableRepository r = repos.getOuttable();
                    r.saveAndFlush(oobj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                p.setOuta(oobj);
            } else {
                oobj = p.getOuta();
            }

            setOut(oobj, outr, outc);
            if (!isNew) {
                PayperiodRepository rp = repos.getPayPeriod();
                rp.saveAndFlush(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setOut(Outtable obj, double outr, double outc) {
        obj.setOutr(outr);
        obj.setOutc(outc);

        try {
            OuttableRepository r = repos.getOuttable();
            r.save(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
