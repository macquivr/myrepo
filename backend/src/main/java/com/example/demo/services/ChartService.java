package com.example.demo.services;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Stype;
import com.example.demo.domain.Category;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.dto.ui.DatasourceDTO;
import com.example.demo.bean.Chart;
import com.example.demo.bean.Lv;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.uidata.ChartUI;

import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Vector;

@Service
public class ChartService {
    private static final Logger logger=LoggerFactory.getLogger(ChartService.class);

    @Autowired
    private LedgerRepository repository;

    @Autowired
    private StypeRepository srepository;

    @Autowired
    private CategoryRepository crepository;

    public DatasourceDTO getChartStypeAtm(String sessionId) { return getChartStype(sessionId, "ATM"); }

    public DatasourceDTO getChartStypeBills(String sessionId) {
        return getChartStype(sessionId, "Bills");
    }

    public DatasourceDTO getChartStypeMisc(String sessionId) {
        return getChartStype(sessionId, "Misc");
    }

    public DatasourceDTO getChartStypePos(String sessionId) {
        return getChartStype(sessionId, "POS");
    }

    public DatasourceDTO getChartStypeAnnual(String sessionId) {
        return getChartStype(sessionId, "Annual");
    }

    public DatasourceDTO getChartStypeCredit(String sessionId) {
        return getChartStype(sessionId, "Credit");
    }
    private DatasourceDTO getChartStype(String sessionId, String stypeName)
    {
        if (sessionId == null) {
            logger.error("No Session.");
            return new DatasourceDTO();
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return new DatasourceDTO();
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return new DatasourceDTO();
        }

        if (filter.getConsolidate() == Consolidate.NONE) {
            logger.info("No consolidate set.");
            return new DatasourceDTO();
        }

        Stype stype = srepository.findByName(stypeName);
        if (stype == null) {
            logger.error("NO stype " + stypeName);
            return new DatasourceDTO();
        }

        LData ld = new LData(repository);
        List<Ledger> data = ld.filterByDate(filter, stype, null);
        StartStop dates = ld.getDates();

        List<Lv> ldata = new Vector<Lv>();
        ChartUI bobj = new ChartUI();
        bobj.go(filter, dates, data, ldata);

        DatasourceDTO ret = new DatasourceDTO();
        Chart c = new Chart();

        c.setTheme("fusion");
        c.setCaption(stypeName);
        c.setXAxisName("Date");
        c.setYAxisName("Amount");

        ret.setChart(c);
        ldata.remove(ldata.size()-1);
        ret.setData(ldata);

        return ret;
    }

    public DatasourceDTO getChartCategory(String sessionId, boolean debit) {
        if (sessionId == null) {
            logger.error("No Session.");
            return new DatasourceDTO();
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return new DatasourceDTO();
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return new DatasourceDTO();
        }

        if (filter.getConsolidate() == Consolidate.NONE) {
            logger.info("No consolidate set.");
            return new DatasourceDTO();
        }

        String cat = filter.getNlcv();
        if (cat == null) {
            logger.info("No Category.");
            return new DatasourceDTO();
        }

        Category category = crepository.findByName(cat);
        if (category == null) {
            logger.info("No Category.");
            return new DatasourceDTO();
        }

        LData ld = new LData(repository);
        List<Ledger> allData = ld.filterByDate(filter, null, null);
        StartStop dates = ld.getDates();
        List<Ledger> data = new Vector<Ledger>();
        for (Ledger l : allData) {
            if (l.getLabel().getCategory().getId() == category.getId()) {
                if (!debit && (l.getAmount() > 0))
                    data.add(l);
                if (debit && (l.getAmount() < 0))
                    data.add(l);
            }
        }

        List<Lv> ldata = new Vector<Lv>();
        ChartUI bobj = new ChartUI();
        bobj.go(filter, dates, data, ldata);

        DatasourceDTO ret = new DatasourceDTO();
        Chart c = new Chart();

        c.setTheme("fusion");
        c.setCaption(category.getName());
        c.setXAxisName("Date");
        c.setYAxisName("Amount");

        ret.setChart(c);
        ldata.remove(ldata.size()-1);
        ret.setData(ldata);

        return ret;
    }

}
