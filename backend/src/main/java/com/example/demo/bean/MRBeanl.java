package com.example.demo.bean;

import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MRBeanl {
    private List<MRBean> data = new ArrayList<MRBean>();
    private MRBeant tabs = new MRBeant();
    private MRBean total = new MRBean("Total",0,0,0);

    public List<MRBean> getData() { return data; }
    public MRBeant getTabs() { return tabs; }

    public void Print(FileWriter w) {
        for (MRBean d : data) {
            d.Print(w,tabs);
        }
        total.Print(w,tabs);
    }
    public double getTotal() { return total.getAmount(); }
    public void add(MRBean b) {
        data.add(b);
        tabs.update(b);
        total.setAmount(Utils.convertDouble(total.getAmount() + b.getAmount()));
        total.setBudget(Utils.convertDouble(total.getBudget() + b.getBudget()));
        total.setNet(Utils.convertDouble(total.getNet() + b.getNet()));
        tabs.update(total);
    }
}
