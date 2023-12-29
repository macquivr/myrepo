package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Budget;
import com.example.demo.domain.Utilities;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.state.Sessions;
import com.example.demo.utils.UtData;
import com.example.demo.utils.dvi.Udvi;
import com.example.demo.utils.idate.BVIDate;
import com.example.demo.utils.idate.Idate;
import com.example.demo.utils.idate.UtilitiesIDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class BVIData extends baseIData {
    private static final Logger logger= LoggerFactory.getLogger(BVIData.class);

    private BudgetRepository repository;
    private SessionDTO filter = null;

    public BVIData(List ldata) {
        this.data = ldata;
    }

    public BVIData(String sessionId, BudgetRepository u) {
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
        Budget u = null;
        if (obj instanceof Budget) {
            u = (Budget) obj;
        } else {
            u = new Budget();
            u.setBdate((LocalDate) obj);
            u.setStmts(null);
            u.setBid(null);
            u.setValue(Double.valueOf(0.0));
            u.setNet(Double.valueOf(0.0));
        }

        return new BVIDate(u);
    }
    public boolean initialize(chartData chartI) {
        /* implement chart later */

        return false;
    }
}
