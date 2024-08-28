package com.example.demo.actions;

import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.repository.PptlmRepository;
import com.example.demo.state.Consolidate;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class PmapAction extends BaseAction implements ActionI {
    private FileWriter w;

    public PmapAction(Repos r) {
        super(r);

    }

    public boolean go(SessionDTO session) throws Exception {
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

        try {
            this.w = new FileWriter("Report.csv");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        if (verify(p)) {
            performAction(p);
        }

        w.flush();
        w.close();
        return true;
    }

    private boolean verify(Payperiod p) throws Exception {
        List<TLedger> tdata = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(p.getStart(), p.getStop());
        List<Ledger> ldata = repos.getLedgerRepository().findAllByTransdateBetweenOrderByTransdateAsc(p.getStart(), p.getStop());

        for (TLedger t : tdata) {
            LocalDate dt = t.getTdate();
            double amount = t.getAmount();
            Ltype lt = t.getLtype();
            Label lbl = t.getLabel();
            boolean found = false;
            for (Ledger l : ldata) {
                if ((dt.toString().equals(l.getTransdate().toString())) &&
                        (amount == l.getAmount()) &&
                        (lt.getId() == l.getLtype().getId()) &&
                        lbl.getId() == l.getLabel().getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                w.write("Ledger not found Tid: " + t.getId() + " " + dt.toString() + " " + amount + " " + lt.getId() + " " + lbl.getId() + "\n");
                return false;
            }
        }
        return performActionC(p,ldata,tdata);
    }

    public void performAction(Payperiod p) throws Exception {
        List<TLedger> tdata = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(p.getStart(), p.getStop());
        List<Ledger> ldata = repos.getLedgerRepository().findAllByTransdateBetweenOrderByTransdateAsc(p.getStart(), p.getStop());
        PptlmRepository r = repos.getPptlmRepository();
        for (TLedger t : tdata) {
            Pptlm obj = new Pptlm();
            obj.setTlid(t.getId());
            LocalDate dt = t.getTdate();
            double amount = t.getAmount();
            Ltype lt = t.getLtype();
            Label lbl = t.getLabel();
            for (Ledger l : ldata) {
                if ((dt.toString().equals(l.getTransdate().toString())) &&
                        (amount == l.getAmount()) &&
                        (lt.getId() == l.getLtype().getId()) &&
                        lbl.getId() == l.getLabel().getId()) {
                    obj.setLid(l.getId());
                    break;
                }
            }
            r.save(obj);
        }
        performActionC(p,ldata,tdata);
    }

    public boolean performActionC(Payperiod p, List<Ledger> ldata, List<TLedger> tdata) throws Exception {
        for (Ledger t : ldata) {
            if ((t.getLtype().getId() == 4) ||
                    (t.getLtype().getId() == 5) ||
                    (t.getLtype().getId() == 6) ||
                    (t.getLtype().getId() == 13)) {
                continue;
            }
            LocalDate dt = t.getTransdate();
            double amount = t.getAmount();
            Ltype lt = t.getLtype();
            Label lbl = t.getLabel();
            boolean found = false;
            for (TLedger l : tdata) {
                if ((dt.toString().equals(l.getTdate().toString())) &&
                        (amount == l.getAmount()) &&
                        (lt.getId() == l.getLtype().getId()) &&
                        lbl.getId() == l.getLabel().getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                w.write("TLedger not found Tid: " + t.getId() + " " + dt.toString() + " " + amount + " " + lt.getId() + " " + lbl.getId() + "\n");
                return false;
            }
        }
        return true;
    }
}

