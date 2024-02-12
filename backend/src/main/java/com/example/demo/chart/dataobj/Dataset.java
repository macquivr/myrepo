package com.example.demo.chart.dataobj;

import java.util.ArrayList;
import java.util.List;

public class Dataset {
    private String seriesName;
    private List<chartValue> data;

    public Dataset() {
        data = new ArrayList<chartValue>();
    }
    public String getSeriesName() {
        return this.seriesName;
    }
    public List<chartValue> getData() {
        return this.data;
    }

    public void setSeriesName(String l) {
        this.seriesName = l;
    }
    public void setData(List<chartValue> d) {
        this.data = d;
    }
}
