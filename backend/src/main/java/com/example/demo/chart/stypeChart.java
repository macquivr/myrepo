package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class stypeChart extends baseChart implements chartData {

    private int stype;

    public stypeChart(int s) {
        this.stype = s;
    }

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : base) {
            if (l.getStype().getId() == this.stype)
                ret.add(l);
        }
        return ret;
    }
}
