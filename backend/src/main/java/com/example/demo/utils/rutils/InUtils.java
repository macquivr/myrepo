package com.example.demo.utils.rutils;

import com.example.demo.bean.Catsort;
import com.example.demo.domain.Ledger;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class InUtils {

    private final List<Catsort> summary;
    private double totalo;

    public InUtils() {
        this.summary = new Vector<>();
        this.totalo = 0;
    }

    public void go(List<Ledger> ldata) {
        for (Ledger l : ldata) {
            Catsort sobj = new Catsort();
            sobj.setLabel(l.getLabel().getName());
            sobj.setAmount(Utils.convertDouble(l.getAmount()));
            this.totalo += sobj.getAmount();
            this.summary.add(sobj);
        }
    }

    public List<Catsort> getData() { return this.summary; }

    public double getTotalo() { return this.totalo; }

}
