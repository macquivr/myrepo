package com.example.demo.utils.idata;

import com.example.demo.chart.chartData;
import com.example.demo.domain.Budget;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.utils.idate.BNIDate;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class BNIData extends baseIData {
    private static final Logger logger= LoggerFactory.getLogger(BNIData.class);

    public BNIData(List ldata) {
        this.data = ldata;
    }

    public BNIData(String sessionId, BudgetRepository u) {
        if (sessionId == null) {
            logger.error("No Session.");
            return;
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
        }
    }

    public Idate factory(Object obj) {
        Budget u;
        if (obj instanceof Budget) {
            u = (Budget) obj;
        } else {
            u = new Budget();
            u.setBdate((LocalDate) obj);
            u.setStmts(null);
            u.setBid(null);
            u.setValue(0.0);
            u.setNet(0.0);
        }

        return new BNIDate(u);
    }
    public boolean initialize(chartData chartI) {
        /* implement chart later */

        return false;
    }
}
