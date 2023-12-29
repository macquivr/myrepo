package com.example.demo.bean;

public class Lvd implements Comparable<Lvd> {
    private String label;
    private Double value;

    public void setLabel(String l) { label = l; }
    public void setValue(Double v) { value = v; }

    public String getLabel() { return label; }
    public Double getValue() { return value; }

    @Override
    public int compareTo(Lvd o) {
        return this.label.compareTo(o.getLabel());
    }
}
