package com.example.demo.chart;

import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.UtilitiesIData;

import java.util.List;

public class utilitiesChart extends baseChart<Utilities> {

    public utilitiesChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new UtilitiesIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
    }
}
