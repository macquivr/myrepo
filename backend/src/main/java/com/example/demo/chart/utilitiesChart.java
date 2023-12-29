package com.example.demo.chart;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Utilities;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.idata.LedgerIData;
import com.example.demo.utils.idata.UtilitiesIData;

import java.util.ArrayList;
import java.util.List;

public class utilitiesChart extends baseChart implements chartData<Utilities> {

    public utilitiesChart(String sessionId, UtilitiesRepository u) {
        this.bidata = new UtilitiesIData(sessionId, u);
    }
    @Override
    public List<Utilities> getChartData(List<Utilities> base) {
        return base;
        /*
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
                    (lbl.getId() == 12196))
                ret.add(l);
        }
        return ret;
         */
    }
}
