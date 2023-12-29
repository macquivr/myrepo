package com.example.demo.utils.uidata;

import com.example.demo.domain.Ledger;
import com.example.demo.domain.Stype;
import com.example.demo.dto.ui.StypeRowDTO;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StypeUI extends Base {

    public Object factory() { return new HashMap<Stype, Double>(); }
    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData().getObj();
        HashMap<Stype, Double> map = (HashMap<Stype,Double>) obj;

        Stype s = l.getStype();

        Double d = map.get(s);
        if (d == null)
            map.put(s, l.getAmount());
        else {
            double dv = d.doubleValue() + l.getAmount().doubleValue();
            Double ndv = new Double(dv);
            map.put(s, ndv);
        }
    }

    public void addStuff(List data, Object obj, String label) {
        HashMap<Stype, Double> map = (HashMap<Stype,Double>) obj;

        StypeRowDTO row = new StypeRowDTO();

        row.setLabel(label);

        Set<Stype> keys = map.keySet();
        double t = 0;
        for (Stype key : keys) {
            if (key.getName().equals("Bills")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setBills(d);
            }

            if (key.getName().equals("Annual")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setAnnual(d);
            }

            if (key.getName().equals("ATM")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setAtm(d);
            }

            if (key.getName().equals("Deposit")) {
                double d = Utils.convertDouble(map.get(key));
                row.setDeposit(d);
            }

            if (key.getName().equals("Misc")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setMisc(d);
            }

            if (key.getName().equals("POS")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setPos(d);
            }

            if (key.getName().equals("Credit")) {
                double d = Utils.convertDouble(map.get(key));
                t += d;
                row.setCredit(d);
            }
        }
        row.setTotal(Utils.convertDouble(t));
        data.add(row);
    }


}
