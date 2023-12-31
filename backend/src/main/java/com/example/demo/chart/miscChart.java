package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Checktype;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Payee;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class miscChart extends baseChart<Ledger> {

    public miscChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,false,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<>();

        for (Ledger l : base) {
            if ((l.getAmount() >= 0) ||
                    (l.getStype().getId() != 5) ||
                    (l.getLtype().getId() != 3))
                continue;

            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                Payee p = c.getPayee();
                Checktype ct = p.getCheckType();
                String ctn = ct.getName();
                if (!ctn.equals("Misc"))
                    continue;
            }
            ret.add(l);
        }
        return ret;
    }
}
