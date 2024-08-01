package com.example.demo.actions;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.TLedger;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.state.Consolidate;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class Pstuff extends BaseAction implements ActionI {
    private FileWriter w;

    public Pstuff(Repos r) {
        super(r);

        try {
            this.w = new FileWriter("Report.csv");
        } catch (Exception ex) {
            // ignore
        }
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

        Payperiod p = null;
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
        w.flush();
        w.close();
        return true;
    }

    public void performAction(Payperiod p) throws Exception {
        List<TLedger> data = repos.getTLedgerRepository().findAllByTdateBetweenOrderByTdateAsc(p.getStart(), p.getStop());

        for (TLedger t : data) {
            w.write(t.getTdate().toString() + " " + t.getId() + " " + t.getAmount() + " " + t.getLtype().getId() + " " + t.getLabel().getName() + "\n");
        }
    }
}
