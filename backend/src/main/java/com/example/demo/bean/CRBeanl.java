package com.example.demo.bean;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CRBeanl {
    private final List<CRBean> data;
    private final CRBeant tabs;
    private final CRBean total;

    public CRBeanl() {
        this.data = new ArrayList<>();
        this.tabs = new CRBeant();
        this.total = new CRBean("Total");
    }
    public List<CRBean> getData() { return this.data; }
    public CRBeant getTabs() { return this.tabs; }

    public void Print(FileWriter w) {
        total.printLabels(w,tabs);
        for (CRBean d : data) {
            d.Print(w,tabs);
        }
        total.Print(w,tabs);
    }

    public void add(CRBean b) {
        data.add(b);
        tabs.update(b);
        total.addUnder(b.getUnder());
        total.addOver(b.getOver());
        total.addPd(b.getPd());
        total.addFree(b.getFree());
        total.addOc(b.getOc());
        total.addOuta(b.getOuta());
        total.addFee(b.getFee());
        total.addNet(b.getNet());
        if (!b.isOk()) {
            total.setOK(false);
        }
        tabs.update(total);
    }
}
