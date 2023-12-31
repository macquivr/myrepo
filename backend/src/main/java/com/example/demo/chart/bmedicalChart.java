package com.example.demo.chart;

import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class bmedicalChart extends baseChart<Ledger> {

    public bmedicalChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<>();

        for (Ledger l : base) {
            if (l.getChecks() != null) {
                if (l.getChecks().getPayee().getId() == 99)
                    ret.add(l);
            }
        }
        return ret;
    }
}
