package com.example.demo.bean;

import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CatSortWithLabels extends Catsort{
    private final List<Ledger> data;
    private final boolean isOther;

    public CatSortWithLabels(Label l, double a,boolean other) {
        data = new ArrayList<>();
        addData(l,a);
        this.isOther = other;
    }

    public void addData(Label label, double amt) {
        Ledger l = new Ledger();
        l.setLabel(label);
        l.setAmount(amt);
        this.data.add(l);
        setAmount(Utils.convertDouble(this.amount + amt));
    }

    public boolean isOther() {
        return this.isOther;
    }
    public List<Ledger> getData() {
        return this.data;
    }
}
