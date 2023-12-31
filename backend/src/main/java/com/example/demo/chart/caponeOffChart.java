package com.example.demo.chart;

import com.example.demo.domain.Category;
import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class caponeOffChart extends baseChart<Ledger> {

    public caponeOffChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<>();

        for (Ledger l : base) {
            if ((l.getLtype().getId() == 7) &&
                    (l.getAmount() < 0) &&
                    (l.getStype().getId() != 8)) {
                Category c = l.getLabel().getCategory();
                if (!((c.getId() == 4) ||
                        (c.getId() == 10) ||
                        (c.getId() == 6) ||
                        (c.getId() == 7))) {
                       ret.add(l);
                }
            }
        }
        return ret;
    }
}
