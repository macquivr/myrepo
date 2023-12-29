package com.example.demo.reports.utils;

import com.example.demo.bean.Catsort;
import com.example.demo.domain.Ledger;

import java.util.*;

import com.example.demo.bean.Lvd;
import com.example.demo.utils.Utils;

public class RMap {
    private List<Ledger> data = null;

    public RMap(List<Ledger> d)
    {
        this.data = d;
    }

    public List<Catsort> apply(RMapI obj) {
        return apply(obj, false);
    }
    public List<Catsort> apply(RMapI obj, boolean check) {
        HashMap<Integer, Ledger> map = new HashMap<Integer,Ledger>();
        List<Catsort> ret = new Vector<Catsort>();

        List<Ledger> death = new Vector<Ledger>();

        for (Ledger l : data) {
            if ((obj == null) || obj.apply(l)) {
                death.add(l);
                if (check) {
                    Ledger m = map.get(l.getChecks().getPayee().getId());
                    if (m == null) {
                        map.put(l.getChecks().getPayee().getId(), l);
                    } else {
                        m.setAmount(m.getAmount() + l.getAmount());
                    }
                } else {
                    Ledger m = map.get(l.getLabel().getId());
                    if (m == null) {
                        map.put(l.getLabel().getId(), l);
                    } else {
                        m.setAmount(m.getAmount() + l.getAmount());
                    }
                }
            }
        }
        Set<Integer> keys = map.keySet();
        for (Integer key : keys) {
            Ledger value = map.get(key);
            Catsort c = new Catsort();
            if (check) {
                c.setLabel(value.getChecks().getPayee().getName());
            } else {
                c.setLabel(value.getLabel().getName());
            }
            c.setAmount(Utils.convertDouble(value.getAmount()));
            ret.add(c);
        }
        data.removeAll(death);
        Collections.sort(ret);
        return ret;
    }
}
