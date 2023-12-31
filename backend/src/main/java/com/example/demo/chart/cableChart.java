package com.example.demo.chart;

import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.CableIData;

import java.util.List;

public class cableChart extends baseChart<Utilities> {

    public cableChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new CableIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
    }
}
