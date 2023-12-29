package com.example.demo.chart;

import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class inOutNetChart extends baseChart implements chartData<Ledger> {

    public inOutNetChart(String sessionId, LedgerRepository l)
    {
        setDontFlip();
        this.bidata = new LedgerIData(l,null,true,sessionId,false);
    }

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : base) {
            if (l.getStype().getId() != 8)
                ret.add(l);
        }
        return ret;
    }
}
