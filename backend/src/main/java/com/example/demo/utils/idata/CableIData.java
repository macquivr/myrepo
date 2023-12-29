package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Utilities;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.UtData;
import com.example.demo.utils.dvi.Cabledvi;
import com.example.demo.utils.dvi.Celldvi;
import com.example.demo.utils.idate.CableIDate;
import com.example.demo.utils.idate.CellIDate;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class CableIData extends baseIData {
    private static final Logger logger= LoggerFactory.getLogger(CableIData.class);

    private UtilitiesRepository repository;
    private SessionDTO filter = null;

    public CableIData(List ldata) {
        this.data = ldata;
    }

    public CableIData(String sessionId, UtilitiesRepository u) {
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
        Utilities u = null;
        if (obj instanceof Utilities) {
            u = (Utilities) obj;
        } else {
            u = new Utilities();
            u.setDate((LocalDate) obj);
            u.setCable(Double.valueOf(0.0));
        }

        return new CableIDate(u);
    }
    public boolean initialize(chartData chartI) {
        UtData ld = new UtData(this.repository);
        List<Utilities> d = ld.filterByDate(this.filter);

        Ldvil<Utilities> ldvil = new Ldvil<Utilities>(d, new Cabledvi(null));
        chartI.filterSpecific(this.filter.getConsolidate(), ldvil);
        this.data = chartI.getChartData(d);
        this.dates = ld.getDates();
        return true;
    }
}
