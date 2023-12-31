package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.idata.LedgerIData;

import java.util.ArrayList;
import java.util.List;

public class regBudgetChart extends baseChart<Ledger> {

    public regBudgetChart(String sessionId, LedgerRepository l) {
        this.bidata = new LedgerIData(l,null,true,sessionId,false);
    }
    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<>();

        for (Ledger l : base) {
            if (l.getLtype().getId() != 3)
                continue;

            if ((l.getStype().getId() == 3) || (l.getStype().getId() == 4))
                ret.add(l);

            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                if (c.getPayee().getId() == 71) {
                    ret.add(l);
                }
                if ((c.getPayee().getId() == 75) ||
                        (c.getPayee().getId() == 60) ||
                        (c.getPayee().getId() == 64)) {
                    ret.add(l);
                }
            }
            Label lbl = l.getLabel();
            if ((lbl.getId() == 10019) ||
                    (lbl.getId() == 11209) ||
                    (lbl.getId() == 10264) ||
                    (lbl.getId() == 10178) ||
                    (lbl.getId() == 12933))
                ret.add(l);

            if (lbl.getId() == 10344)
                ret.add(l);
        }
        return ret;
    }
}
