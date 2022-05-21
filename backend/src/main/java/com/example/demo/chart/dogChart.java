package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class dogChart extends baseChart implements chartData {

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                if (c.getPayee().getId() == 71) {
                    ret.add(l);
                }
            }
        }
        return ret;
    }
}
