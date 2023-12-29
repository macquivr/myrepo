package com.example.demo.chart;

import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class bmortgageChart extends baseChart implements chartData<Ledger> {

    public bmortgageChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if ((l.getLabel().getId() == 11182)  ||
                (l.getLabel().getId() == 10949)  ||
                (l.getLabel().getId() == 12712)) {
                ret.add(l);
            }
        }
        return ret;
    }
}