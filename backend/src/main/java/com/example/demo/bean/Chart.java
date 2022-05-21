package com.example.demo.bean;

public class Chart {
    private String caption = null;
    private String xAxisName = null;
    private String yAxisName = null;
    private String theme = null;

    private String yAxisMinValue = null;

    private String yAxisMaxValue = null;

    public void setCaption(String c) { caption = c; }
    public void setXAxisName(String x) { xAxisName = x; }
    public void setYAxisName(String y) { yAxisName = y; }

    public void setYAxisMinValue(String y) { yAxisMinValue = y; }

    public void setYAxisMaxValue(String y) { yAxisMaxValue = y; }

    public void setTheme(String t) { theme = t; }

    public String getCaption() { return caption; }
    public String getXAxisName() { return xAxisName; }
    public String getYAxisName() { return yAxisName; }

    public String getYAxisMinValue() { return yAxisMinValue; }

    public String getYAxisMaxValue() { return yAxisMaxValue; }

    public String getTheme() { return theme; }

}
