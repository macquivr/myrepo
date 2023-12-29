package com.example.demo.dto.ui;

import com.example.demo.bean.*;

import java.util.List;

public class DatasourceDTO {
    private Chart chart;
    private List<Lvd> data;
    private List<TrendLine> trendlines;

    public void setChart(Chart c) { chart = c; }
    public Chart getChart() { return chart; }

    public void setData(List<Lvd> d) { data = d; }
    public List<Lvd> getData() { return data; }

    public void setTrendlines(List<TrendLine> v) { trendlines = v; }
    public List<TrendLine> getTrendlines() { return trendlines; }
}
