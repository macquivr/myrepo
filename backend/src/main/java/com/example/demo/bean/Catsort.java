package com.example.demo.bean;

public class Catsort implements Comparable<Catsort> {
    private int dir = 1;
    private String label;
    protected Double amount;

    public Catsort() {
        this.amount = Double.valueOf(0);
    }
    public void reverse() {
        this.dir = -1;
    }
    public void setLabel(String l) { this.label = l; }
    public void setAmount(double d) { this.amount = d; }

    public String getLabel() { return this.label; }
    public double getAmount() { return this.amount; }

    @Override
    public int compareTo(Catsort o) {
        int ret = this.amount.compareTo(o.getAmount());

        ret = ret * this.dir;

        return ret;
    }

}
