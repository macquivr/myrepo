package com.example.demo.bean;

public class nbBean {
    private String label;
    private Double current;
    private Double change;

    public nbBean(String l) {
        this.label = l;
    }

    public void setCurrent(double c) {
        this.current = c;
    }

    public void setChange(double ch) {
        this.change = ch;
    }

    public String getLabel() { return this.label; }
    public Double getCurrent() { return this.current; }
    public Double getChange() { return this.change; }
}
