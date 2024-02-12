package com.example.demo.bean;

public class ChartMs {

    private String caption = null;
    private String yaxisname = null;
    private String subcaption = null;
    private String showhovereffect = null;

    private String numbersuffix = null;
    private String drawcrossline = null;
    private String plottooltext = null;

    private String theme = null;

    public void setCaption(String c) { this.caption = c; }
    public void setYaxisname(String y) { this.yaxisname = y; }
    public void setSubcaption(String c) { this.subcaption  = c; }
    public void setShowhovereffect(String s) { this.showhovereffect  = s; }
    public void setNumbersuffix(String s) { this.numbersuffix = s; }
    public void setDrawcrossline(String d) { this.drawcrossline = d; }
    public void setPlottooltext(String t) { this.plottooltext = t; }
    public void setTheme(String t) { this.theme = t; }

    public String getCaption() { return this.caption; }
    public String getYaxisname() { return this.yaxisname; }
    public String getSubcaption() { return this.subcaption; }
    public String getShowhovereffect() { return this.showhovereffect; }
    public String getNumbersuffix() { return this.numbersuffix; }
    public String getDrawcrossline() { return this.drawcrossline; }
    public String getPlottooltext() { return this.plottooltext; }
    public String getTheme() { return this.theme; }


}
