package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class posChart extends baseChart implements chartData {

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : base) {
            if (l.getStype().getId() == 3)
                ret.add(l);
        }
        return ret;
    }
}
