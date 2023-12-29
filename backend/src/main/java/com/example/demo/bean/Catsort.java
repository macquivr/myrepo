package com.example.demo.bean;

public class Catsort implements Comparable<Catsort> {
    private String label;
    private Double amount;

    public void setLabel(String l) { this.label = l; }
    public void setAmount(double d) { this.amount = d; }

    public String getLabel() { return this.label; }
    public double getAmount() { return this.amount; }

    @Override
    public int compareTo(Catsort o) { return this.amount.compareTo(o.getAmount()); }
}
