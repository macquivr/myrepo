package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Utilities;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.UtData;
import com.example.demo.utils.dvi.Elecdvi;
import com.example.demo.utils.idate.ElecIDate;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ElecIData extends baseIData<Utilities> {
    private static final Logger logger= LoggerFactory.getLogger(ElecIData.class);

    private UtilitiesRepository repository;
    private SessionDTO filter = null;

    public ElecIData(List ldata) {
        this.data = ldata;
    }

    public ElecIData(String sessionId, UtilitiesRepository u) {
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
        Utilities u;
        if (obj instanceof Utilities) {
            u = (Utilities) obj;
        } else {
            u = new Utilities();
            u.setDate((LocalDate) obj);
            u.setElectric(0.0);
        }

        return new ElecIDate(u);
    }
    public boolean initialize(chartData<Utilities> chartI) {
        UtData ld = new UtData(this.repository);
        List<Utilities> d = ld.filterByDate(this.filter);

        Ldvil<Utilities> ldvil = new Ldvil<>(d, new Elecdvi(null));
        chartI.filterSpecific(this.filter.getConsolidate(), ldvil);
        this.data = chartI.getChartData(d);
        this.dates = ld.getDates();
        return true;
    }
}
