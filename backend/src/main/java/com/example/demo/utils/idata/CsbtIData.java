package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Csbt;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.CsbtRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.CsbtData;
import com.example.demo.utils.dvi.Csbtdvi;
import com.example.demo.utils.idate.CsbtIDate;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class CsbtIData extends baseIData<Csbt> {
    private static final Logger logger= LoggerFactory.getLogger(CsbtIData.class);

    private CsbtRepository repository;
    private SessionDTO filter = null;

    public CsbtIData(List ldata) {
        this.data = ldata;
    }

    public CsbtIData(String sessionId, CsbtRepository u) {
        if (sessionId == null) {
            logger.error("No Session.");
            return;
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return;
        }

        this.filter = Sessions.getObj().getSession(sessionId);
        this.repository = u;
    }

    public Idate factory(Object obj) {
        Csbt u;
        if (obj instanceof Csbt) {
            u = (Csbt) obj;
        } else {
            u = new Csbt();
            u.setDt((LocalDate) obj);
            u.setBalance(0.0);
        }

        return new CsbtIDate(u);
    }
    public boolean initialize(chartData<Csbt> chartI) {
        CsbtData ld = new CsbtData(this.repository);
        List<Csbt> d = ld.filterByDate(this.filter);

        Ldvil<Csbt> ldvil = new Ldvil<>(d, new Csbtdvi(null));
        chartI.filterSpecific(this.filter.getConsolidate(), ldvil);
        this.data = chartI.getChartData(d);
        this.dates = ld.getDates();
        return true;
    }
}
