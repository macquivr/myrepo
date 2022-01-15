package com.example.demo.bean;

import com.example.demo.utils.*;

public class Ion {
    private String label = null;
    private double ina = 0.0;
    private double out = 0.0;
    private double net = 0.0;

    public void setLabel(String s) { label = s; }
    public void setIn(double d) { ina = Utils.convertDouble(d); }
    public void setOut(double d) { out = Utils.convertDouble(d); }
    public void setNet(double d) { net = Utils.convertDouble(d); }

    public String getLabel() { return label; }
    public double getIn() { return ina; }
    public double getOut() { return out; }
    public double getNet() { return net; }

}
