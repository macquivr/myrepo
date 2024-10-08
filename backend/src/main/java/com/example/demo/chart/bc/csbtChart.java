package com.example.demo.chart.bc;

import com.example.demo.chart.baseChart;
import com.example.demo.domain.Csbt;
import com.example.demo.repository.CsbtRepository;
import com.example.demo.chart.idata.CsbtIData;

import java.util.List;

public class csbtChart extends baseChart<Csbt> {

    public csbtChart(String sessionId, CsbtRepository u) {
        this.bidata = new CsbtIData(sessionId, u);
        setDontFlip();
    }
    @Override
    public List<Csbt> getChartData(List<Csbt> base) {
        return base;
    }
}
