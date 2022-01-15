package com.example.demo.bean;

public class Catsort implements Comparable<Catsort> {
    private String label;
    private double amount;

    public void setLabel(String l) { label = l; }
    public void setAmount(double d) { amount = d; }

    public String getLabel() { return label; }
    public double getAmount() { return amount; }

    @Override
    public int compareTo(Catsort o) {
        if (o.getAmount() == amount)
            return 0;
        if (o.getAmount() < amount)
            return 1;
        return -1;
    }
}
