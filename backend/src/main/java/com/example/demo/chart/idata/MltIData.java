package com.example.demo.chart.idata;

import com.example.demo.chart.chartData;
import com.example.demo.chart.data.MltData;
import com.example.demo.chart.dvi.Mltdvi;
import com.example.demo.chart.idate.MltIDate;
import com.example.demo.domain.Mlt;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.MltRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.idata.Ldvil;
import com.example.demo.utils.idata.baseIData;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class MltIData extends baseIData<Mlt> {
    private static final Logger logger= LoggerFactory.getLogger(MltIData.class);

    private MltRepository repository;
    private SessionDTO filter = null;

    public MltIData(List ldata) {
        this.data = ldata;
    }

    public MltIData(String sessionId, MltRepository u) {
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
        Mlt u;
        if (obj instanceof Mlt) {
            u = (Mlt) obj;
        } else {
            u = new Mlt();
            u.setDt((LocalDate) obj);
            u.setBalance(0.0);
        }

        return new MltIDate(u);
    }
    public boolean initialize(chartData<Mlt> chartI) {
        MltData ld = new MltData(this.repository);
        List<Mlt> d = ld.filterByDate(this.filter);

        Ldvil<Mlt> ldvil = new Ldvil<>(d, new Mltdvi(null));
        chartI.filterSpecific(this.filter.getConsolidate(), ldvil);
        this.data = chartI.getChartData(d);
        this.dates = ld.getDates();
        return true;
    }
}
