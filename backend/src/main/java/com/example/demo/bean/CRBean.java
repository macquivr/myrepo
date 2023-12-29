package com.example.demo.bean;

import com.example.demo.utils.Utils;

import java.io.FileWriter;

public class CRBean {
    private String label;
    private double under;
    private double over;

    private double pd;
    private double free;
    private double oc;

    private double outa;

    private double fee;

    private double net;
    private boolean ok;

    public CRBean(String l) {
        this.label = l;
        this.ok = true;
    }
    public String getLabel() { return label; }
    public void setLabel(String l) { label = l;}

    public double getPd() { return this.pd; }
    public void addPd(double d) {
        this.pd = Utils.convertDouble(this.pd + d);
    }

    public double getFree() { return this.free; }
    public void addFree(double d) {
        this.free = Utils.convertDouble(this.free + d);
    }

    public double getOc() { return this.oc; }
    public void addOc(double d) {
        this.oc = Utils.convertDouble(this.oc + d);
    }

    public double getOuta() { return this.outa; }
    public void addOuta(double d) { this.outa = Utils.convertDouble(this.outa + d); }

    public double getFee() { return this.fee; }
    public void addFee(double d) { this.fee = Utils.convertDouble(this.fee + d); }

    public double getNet() { return this.net; }
    public void addNet(double d) { this.net = Utils.convertDouble(this.net + d); }

    public double getUnder() { return this.under; }
    public void addUnder(double d) { this.under = Utils.convertDouble(this.under + d); }

    public double getOver() { return this.over; }
    public void addOver(double d) { this.over = Utils.convertDouble(this.over + d); }

    public boolean isOk() { return this.ok; }
    public void setOK(boolean d) { this.ok = d; }

    private void P(FileWriter w, String label, int t) throws Exception {
        w.write(label);
        ptabs(w,t, label.length());
    }

    public void printLabels(FileWriter w,CRBeant t)  {
        try {
            P(w, "label", t.getLabel());
            P(w, "Under", t.getUnder());
            P(w, "Over", t.getOver());
            P(w, "Pd", t.getPd());
            P(w, "Free", t.getFree());
            P(w, "Oc", t.getOc());
            P(w, "Out", t.getOuta());
            P(w, "Fee", t.getFee());
            w.write("Net\n");
        } catch (Exception ex) {
            // ignore
        }
    }
    public void Print(FileWriter w, CRBeant t) {
        try {
            w.write(label);
            ptabs(w,t.getLabel(), label.length());

            w.write(String.valueOf(this.under));
            ptabs(w,t.getUnder(),String.valueOf(this.under).length());

            w.write(String.valueOf(this.over));
            ptabs(w,t.getOver(),String.valueOf(this.over).length());

            w.write(String.valueOf(this.pd));
            ptabs(w,t.getPd(),String.valueOf(this.pd).length());
            w.write(String.valueOf(this.free));
            ptabs(w,t.getFree(),String.valueOf(this.free).length());
            w.write(String.valueOf(this.oc));
            ptabs(w,t.getOc(),String.valueOf(this.oc).length());
            w.write(String.valueOf(this.outa));
            ptabs(w,t.getOuta(),String.valueOf(this.outa).length());
            w.write(String.valueOf(this.fee));
            ptabs(w,t.getFee(),String.valueOf(this.fee).length());

            w.write(net + "\n");
        } catch (Exception ex) {
            // ignore
        }
    }

    public void calculateDiff(double sbal, double ina) {
        double diff = Utils.convertDouble(sbal - ina);

        if (diff == 0) {
            this.ok = true;
        } else {
            this.ok = false;
            if (diff < 0) {
                addOver(diff * (-1));
            } else {
                addUnder(diff);
            }
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
