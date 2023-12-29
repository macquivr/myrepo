package com.example.demo.chart.bcredit;

import com.example.demo.chart.baseChart;
import com.example.demo.chart.chartData;
import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class bamazonChart extends baseChart implements chartData<Ledger> {
    public bamazonChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if (l.getLabel().getId() == 10019) {
                ret.add(l);
            }
        }
        return ret;
    }
}
