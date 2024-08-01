package com.example.demo.actions;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Intable;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Outtable;
import com.example.demo.domain.Payperiod;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.OuttableRepository;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.Utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class PpAction extends BaseAction implements ActionI{
    private HashMap<Integer,Integer> inmap = null;

    public PpAction(Repos r) {
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
        return true;
    }

    public void performAction(Payperiod p) {
        Intable ina = p.getIna();
        Outtable outa = p.getOuta();

        System.out.println("In: " + ina.getId() + " Out: " + outa.getId());
    }


}
