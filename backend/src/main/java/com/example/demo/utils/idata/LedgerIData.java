package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Stype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.LData;
import com.example.demo.utils.dvi.Ldvi;
import com.example.demo.utils.idate.Idate;
import com.example.demo.utils.idate.LedgerIDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class LedgerIData extends baseIData {
    private static final Logger logger= LoggerFactory.getLogger(LedgerIData.class);

    private LedgerRepository repository;
    private StypeRepository srepository;

    private boolean bundle = false;

    private SessionDTO filter = null;

    private boolean doUtil = false;

    public LedgerIData(List ldata) {
        this.data = ldata;
    }
    public LedgerIData(LedgerRepository lr, StypeRepository sr, boolean b, String sessionId, boolean du)
    {
        if (sessionId == null) {
            logger.error("No Session.");
            return;
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return;
        }

        this.filter = Sessions.getObj().getSession(sessionId);
        this.repository = lr;
        this.srepository = sr;
        this.bundle = b;
        this.doUtil = du;
    }

    public Idate factory(Object obj) {
        Ledger l = null;
        if (obj instanceof Ledger) {
            l = (Ledger) obj;
        } else {
          l = new Ledger();
          l.setTransdate((LocalDate) obj);
          l.setAmount(Double.valueOf(0.0));
        }

        return new LedgerIDate(l);
    }

    public boolean initialize(chartData chartI) {
        if (this.filter == null) {
            return false;
        }
        Stype stype = null;
        if (doUtil) {
            stype = srepository.findByName("Utils");
            if (stype == null) {
                logger.error("NO stype Utils");
                return false;
            }
        }
        LData ld = new LData(this.repository);
        List<Ledger> d = ld.filterByDate(this.filter, stype, null);
        if (this.bundle) {
            ld.filterBundle(d);
        }

        Ldvil<Ledger> ldvil = new Ldvil<Ledger>(d, new Ldvi(null));
        chartI.filterSpecific(this.filter.getConsolidate(), ldvil);
        this.data = chartI.getChartData(d);
        this.dates = ld.getDates();
        return true;
    }
}
