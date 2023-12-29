package com.example.demo.bean;

import com.example.demo.utils.*;

public class Ion {
    private String label = null;
    private double ina = 0.0;
    private double out = 0.0;
    private double tin = 0.0;
    private double tout = 0.0;

    private double net = 0.0;

    private double balance = 0;

    private Data d = null;

    public void setBalance(double b) { this.balance = b; }
    public void setData(Data data) { this.d = data; }
    public void setLabel(String s) { label = s; }
    public void setIn(double d) { ina = Utils.convertDouble(d); }
    public void setOut(double d) { out = Utils.convertDouble(d); }

    public void setTIn(double d) { tin = Utils.convertDouble(d); }
    public void setTOut(double d) { tout = Utils.convertDouble(d); }

    public void setNet(double d) { net = Utils.convertDouble(d); }

    public double getBalance() { return this.balance; }
    public Data getData() { return d; }
    public String getLabel() { return label; }
    public double getIn() { return ina; }
    public double getOut() { return out; }

    public double getTIn() { return tin; }
    public double getTOut() { return tout; }
    public double getNet() { return net; }

    public double getTNet() { return Utils.convertDouble(ina + out); }

}
