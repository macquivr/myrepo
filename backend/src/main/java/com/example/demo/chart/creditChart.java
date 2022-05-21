package com.example.demo.chart;

import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class creditChart extends baseChart implements chartData {

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if ((l.getAmount() >= 0) || (l.getStype().getId() == 8))
                continue;

            if ((l.getLtype().getId() == 10) ||
                    (l.getLtype().getId() == 9) ||
                    (l.getLtype().getId() == 8) ||
                    (l.getLtype().getId() == 7)) {
                ret.add(l);
            }
        }
        return ret;
    }
}
