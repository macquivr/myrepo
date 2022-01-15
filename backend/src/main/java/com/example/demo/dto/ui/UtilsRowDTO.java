package com.example.demo.dto.ui;

public class UtilsRowDTO {
    private String label;
    private double cable;
    private double cell;
    private double electric;

    public String getLabel() { return label; }
    public double getCable() { return cable; }
    public double getCell() { return cell; }
    public double getElectric() { return electric; }

    public void setLabel(String s) { label = s; }
    public void setCable(double d) { cable = d; }
    public void setCell(double d) { cell = d; }
    public void setElectric(double d) { electric = d; }
}
