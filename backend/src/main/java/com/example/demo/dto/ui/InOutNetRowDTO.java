package com.example.demo.dto.ui;

public class InOutNetRowDTO {
    private String label;
    private double inAmt;
    private double outAmt;
    private double net;

    public String getLabel() { return label; }
    public double getInAmt() { return inAmt; }
    public double getOutAmt() { return outAmt; }
    public double getNet() { return net; }

    public void setLabel(String s) { label = s; }
    public void setInAmt(double d) { inAmt = d; }
    public void setOutAmt(double d) { outAmt = d; }
    public void setNet(double d) { net = d; }
}
