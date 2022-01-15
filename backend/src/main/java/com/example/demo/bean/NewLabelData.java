package com.example.demo.bean;

public class NewLabelData {
    private String date;
    private String label;
    private String type;

    public String getDate() { return this.date; }
    public void setDate(String d) { this.date = d; }

    public String getLabel() { return type + " " + label; }
    public void setLabel(String l) { this.label = l;}

    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
}
