package com.example.demo.bean;

import java.io.FileWriter;

public class MRBean {
    private String label;
    private double amount;
    private double budget;
    private double net;

    private double other;

    public MRBean(String l, double a, double b, double n) {
        this.label = l;
        this.amount = a;
        this.budget = b;
        this.net = n;
    }
    public String getLabel() { return label; }
    public void setLabel(String l) { label = l;}

    public double getOther() { return other; }
    public void setOther(double d) { this.other = d; }
    public double getAmount() { return amount; }
    public void setAmount(double a) { amount = a; }

    public double getBudget() { return budget; }
    public void setBudget(double b) { budget = b; }

    public double getNet() { return net; }
    public void setNet(double n) { net = n; }

    public void Print(FileWriter w, MRBeant t) {
        try {
            w.write(label);
            ptabs(w,t.getLabel(), label.length());
            w.write(String.valueOf(amount));
            ptabs(w,t.getAmount(),String.valueOf(amount).length());
            w.write(String.valueOf(budget));
            ptabs(w,t.getBudget(),String.valueOf(budget).length());
            w.write(String.valueOf(net));
            w.write("\n");
        } catch (Exception ex) {
            // ignore
        }
    }

    private void ptabs(FileWriter w, int max, int len) throws Exception
    {
        int mt = (max / 8);
        int ll = (len / 8);
        int pt = (mt - ll) + 1;
        for (int i = 0;i<pt;i++)
            w.write("\t");
    }
}
