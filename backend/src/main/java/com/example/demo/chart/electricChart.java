package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.ElecIData;
import com.example.demo.utils.idata.UtilitiesIData;

import java.util.ArrayList;
import java.util.List;

public class electricChart extends baseChart implements chartData<Utilities> {

    public electricChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new ElecIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
        /*
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                if (c.getPayee().getId() == 75) {
                    ret.add(l);
                }
            }
            Label lbl = l.getLabel();
            if ((lbl.getId() == 12660) || (lbl.getId() == 11281))
                ret.add(l);
        }
        return ret;
         */
    }
}
