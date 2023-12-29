package com.example.demo.chart;

import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.CellIData;
import com.example.demo.utils.idata.ElecIData;

import java.util.List;

public class cellChart extends baseChart implements chartData<Utilities> {

    public cellChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new CellIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
    }
}
