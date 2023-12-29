package com.example.demo.dto.ui;

public class CStatusRowDTO {
    private String name;
    private double over;
    private double under;
    private double dr;
    private double netfree;

    public String getName() { return name; }
    public double getOver() { return over; }
    public double getUnder() { return under; }
    public double getDr() { return dr; }
    public double getNetfree() { return netfree; }

    public void setName(String n) { name = n; }
    public void setOver(double d) { over = d; }
    public void setUnder(double d) { under = d; }
    public void setDr(double d) { dr = d; }

    public void setNetfree(double d) { netfree = d; }
}
