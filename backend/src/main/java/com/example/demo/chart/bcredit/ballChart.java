package com.example.demo.chart.bcredit;

import com.example.demo.chart.baseChart;
import com.example.demo.chart.chartData;
import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class ballChart extends baseChart implements chartData<Ledger> {
    public ballChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if ((l.getLabel().getId() == 11209) ||
                            (l.getLabel().getId() == 10019) ||
                            (l.getLabel().getId() == 10264) ||
                            (l.getLabel().getId() == 12933)
            ) {
                ret.add(l);
            } else {
                if (l.getChecks() != null) {
                    if (l.getChecks().getPayee().getId() == 116)
                        ret.add(l);
                }
            }
        }
        return ret;
    }
}
