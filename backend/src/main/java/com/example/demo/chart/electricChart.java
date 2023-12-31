package com.example.demo.chart;

import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.ElecIData;

import java.util.List;

public class electricChart extends baseChart<Utilities> {

    public electricChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new ElecIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
    }
}
