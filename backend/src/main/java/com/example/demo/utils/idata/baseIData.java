package com.example.demo.utils.idata;

import com.example.demo.bean.StartStop;
import com.example.demo.chart.chartData;
import com.example.demo.utils.idate.Idate;

import java.util.List;

public abstract class baseIData<T> {
    protected List data;

    protected StartStop dates;

    public abstract Idate factory(Object obj);
    public abstract boolean initialize(chartData<T> chartI);

    public void setDates(StartStop d) {
        this.dates = d;
    }
    public List getData() {
        return this.data;
    }

    public StartStop getDates() {
        return this.dates;
    }
}
