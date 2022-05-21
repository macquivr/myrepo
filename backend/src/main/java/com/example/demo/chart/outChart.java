package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class outChart extends baseChart implements chartData {

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : base) {
            if ((l.getAmount() < 0) && (l.getStype().getId() != 8))
                ret.add(l);
        }
        return ret;
    }
}
