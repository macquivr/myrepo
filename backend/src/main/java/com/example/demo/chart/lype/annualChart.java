package com.example.demo.chart.lype;

import com.example.demo.chart.baseChart;
import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class annualChart extends baseChart<Ledger> {

    public annualChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,true,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<>();
        for (Ledger l : base) {
            if ((l.getAmount() < 0) &&
                    (l.getLtype().getId() == 14) &&
                    (l.getStype().getId() != 8)
            )
                ret.add(l);
        }
        return ret;
    }
}
