package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Budgets;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.BudgetsRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.idate.BVIDate;
import com.example.demo.utils.idate.BVSIDate;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class BVSIData extends baseIData {
    private static final Logger logger= LoggerFactory.getLogger(BVSIData.class);

    private BudgetsRepository repository;
    private SessionDTO filter = null;

    public BVSIData(List ldata) {
        this.data = ldata;
    }

    public BVSIData(String sessionId, BudgetsRepository u) {
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
        Budgets u = null;
        if (obj instanceof Budgets) {
            u = (Budgets) obj;
        } else {
            u = new Budgets();
            u.setBdate((LocalDate) obj);
            u.setStmts(null);
            u.setBid(null);
            u.setValue(Double.valueOf(0.0));
            u.setNet(Double.valueOf(0.0));
        }

        return new BVSIDate(u);
    }
    public boolean initialize(chartData chartI) {
        /* implement chart later */

        return false;
    }
}
