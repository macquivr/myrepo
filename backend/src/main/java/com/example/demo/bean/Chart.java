package com.example.demo.bean;

public class Chart {
    private String caption = null;
    private String xAxisName = null;
    private String yAxisName = null;
    private String theme = null;

    public void setCaption(String c) { caption = c; }
    public void setXAxisName(String x) { xAxisName = x; }
    public void setYAxisName(String y) { yAxisName = y; }
    public void setTheme(String t) { theme = t; }

    public String getCaption() { return caption; }
    public String getXAxisName() { return xAxisName; }
    public String getYAxisName() { return yAxisName; }
    public String getTheme() { return theme; }

}
