package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class inOutNetChart extends baseChart implements chartData {

    public inOutNetChart()
    {
        setDontFlip();
    }

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : base) {
            if (l.getStype().getId() != 8)
                ret.add(l);
        }
        return ret;
    }
}
