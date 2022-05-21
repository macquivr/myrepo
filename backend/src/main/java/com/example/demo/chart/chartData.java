package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.List;

public interface chartData {

    public List<Ledger> getChartData(List<Ledger> base);
    public boolean dontFlip();
    public Double getNetMod();
}
