package com.example.demo.bean;

public class MRBeant {
    private int label;
    private int amount;
    private int budget;

    public MRBeant() {
        label = -1;
        amount = -1;
        budget = -1;
    }
    public int getLabel() { return label; }
    public void setLabel(int l) { label = l; }

    public int getAmount() { return amount; }
    public void setAmount(int a) { amount = a; }

    public int getBudget() { return budget; }
    public void setBudget(int b) { budget = b; }

    public void update(MRBean b) {
        String l = b.getLabel();
        if ((label == -1) || (l.length() > label)) {
            label = l.length();
        }

        double a = b.getAmount();
        int len = String.valueOf(a).length();
        if ((amount == -1) || (len > amount)) {
            amount = len;
        }

        double bu = b.getBudget();
        len = String.valueOf(bu).length();
        if ((budget == -1) || (len > budget)) {
            budget = len;
        }
    }
}
