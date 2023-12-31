package com.example.demo.bean;

public class CRBeant {
    private int label;

    private int under;
    private int over;
    private int pd;
    private int free;
    private int oc;
    private int outa;

    private int fee;
    public CRBeant() {
        under = -1;
        over = -1;
        label = -1;
        pd = -1;
        free = -1;
        oc = -1;
        outa = -1;
        fee = -1;
    }
    public int getLabel() { return label; }
    public void setLabel(int l) { label = l; }

    public int getUnder() { return this.under; }
    public void setUnder(int b) { this.under = b; }

    public int getOver() { return this.over; }
    public void setOver(int b) { this.over = b; }

    public int getPd() { return this.pd; }
    public void setPd(int b) { this.pd = b; }

    public int getFree() { return this.free; }
    public void setFree(int b) { this.free = b; }

    public int getOc() { return this.oc; }
    public void setOc(int b) { this.oc = b; }

    public int getOuta() { return this.outa; }
    public void setOuta(int b) { this.outa = b; }

    public int getFee() { return this.fee; }
    public void setFee(int b) { this.fee = b; }


    public void update(CRBean b) {
        int len;
        String l = b.getLabel();
        if ((label == -1) || (l.length() > label)) {
            label = l.length();
        }

        double u = b.getUnder();
        len = String.valueOf(u).length();
        if ((this.under == -1) || (len > this.under)) {
            this.under = len;
        }

        double o = b.getOver();
        len = String.valueOf(o).length();
        if ((this.over == -1) || (len > this.over)) {
            this.over = len;
        }

        double bu = b.getPd();
        len = String.valueOf(bu).length();
        if ((this.pd == -1) || (len > this.pd)) {
            this.pd = len;
        }

        double f = b.getFree();
        len = String.valueOf(f).length();
        if ((this.free == -1) || (len > this.free)) {
            this.free = len;
        }

        double oc = b.getOc();
        len = String.valueOf(oc).length();
        if ((this.oc == -1) || (len > this.oc)) {
            this.oc = len;
        }

        double outa = b.getOuta();
        len = String.valueOf(outa).length();
        if ((this.outa == -1) || (len > this.outa)) {
            this.outa = len;
        }

        double fee = b.getFee();
        len = String.valueOf(fee).length();
        if ((this.fee == -1) || (len > this.fee)) {
            this.fee = len;
        }
    }
}
