package com.example.demo.reports.utils;

import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.Gscat;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.TLedger;
import com.example.demo.repository.GscatRepository;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.*;

public class InUtilsR {
    private GscatRepository grepo = null;

    public InUtilsR(GscatRepository gr) {
        this.grepo = gr;
    }

    public HashMap<String, Catsort> doIn(HashMap<Integer,Integer> inmap, List<TLedger> data) throws Exception {
        List<TLedger> inl = new ArrayList<TLedger>();

        for (TLedger l : data) {
            if (l.getAmount() > 0)
                inl.add(l);
        }
        HashMap<String, Catsort> inm = new HashMap<String, Catsort>();
        for (TLedger l : inl) {
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
            Catsort r = putm(inm,n,l.getAmount());
            r.reverse();
        }
        return inm;
    }

    public Catsort putm(HashMap<String, Catsort> map,String key, double amount) {
        Catsort d = map.get(key);
        if (d == null) {
            d = new Catsort();
            d.setLabel(key);
            d.setAmount(amount);
            map.put(key, d);
        } else {
            d.setAmount(Utils.convertDouble(d.getAmount() + amount));
        }
        return d;
    }


}
