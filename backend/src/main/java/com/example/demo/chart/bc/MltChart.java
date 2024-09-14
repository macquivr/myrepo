package com.example.demo.chart.bc;

import com.example.demo.chart.baseChart;
import com.example.demo.chart.idata.MltIData;
import com.example.demo.domain.Mlt;
import com.example.demo.repository.MltRepository;

import java.util.List;

public class MltChart extends baseChart<Mlt> {

    public MltChart(String sessionId, MltRepository u) {
        this.bidata = new MltIData(sessionId, u);
        setDontFlip();
    }
    @Override
    public List<Mlt> getChartData(List<Mlt> base) {
        return base;
    }
}
