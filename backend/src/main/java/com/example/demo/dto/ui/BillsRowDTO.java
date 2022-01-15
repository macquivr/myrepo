package com.example.demo.dto.ui;

public class BillsRowDTO {
    private String label;
    private double utilities;
    private double mortg;
    private double life;
    private double car;
    private double emma;
    private double misc;

    public String getLabel() { return label; }
    public double getUtilities() { return utilities; }
    public double getMortg() { return mortg; }
    public double getLife() { return life; }
    public double getMisc() { return misc; }
    public double getCar() { return car; }
    public double getEmma() { return emma; }


    public void setLabel(String l) { label = l; }
    public void setUtilities(double d) { utilities = d; }
    public void setMortg(double d) { mortg = d; }
    public void setLife(double d) { life = d; }
    public void setMisc(double d) { misc = d; }
    public void setCar(double d) { car = d; }
    public void setEmma(double d) { emma = d; }
}
