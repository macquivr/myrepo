package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;

import java.util.ArrayList;
import java.util.List;

public class utilitiesChart extends baseChart implements chartData {

    @Override
    public List<Ledger> getChartData(List<Ledger> base) {
        List<Ledger> ret = new ArrayList<Ledger>();

        for (Ledger l : base) {
            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                if ((c.getPayee().getId() == 75) ||
                        (c.getPayee().getId() == 60) ||
                        (c.getPayee().getId() == 64)) {
                    ret.add(l);
                }
            }
            Label lbl = l.getLabel();
            if ((lbl.getId() == 12660) ||
                    (lbl.getId() == 11281) ||
                    (lbl.getId() == 10251) ||
                    (lbl.getId() == 10064) ||
                    ((lbl.getId() == 12196) && (l.getAmount() > -100)))
                ret.add(l);
        }
        return ret;
    }
}
