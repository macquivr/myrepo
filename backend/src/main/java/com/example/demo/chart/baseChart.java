package com.example.demo.chart;

public class baseChart
{
    private boolean dflip = false;
    private Double netMod = null;

    public Double getNetMod() { return this.netMod; }
    public void setNetMod(Double d) { this.netMod = d; }
    public void setDontFlip() { this.dflip = true; }
    public boolean dontFlip() { return this.dflip; }
}
