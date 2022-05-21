package com.example.demo.services;

import com.example.demo.bean.*;
import com.example.demo.chart.*;
import com.example.demo.chart.net.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.dto.ui.DatasourceDTO;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.uidata.ChartUI;

import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private List<TrendLine> getTrendLine(String value) {
        List<TrendLine> rl = new ArrayList<TrendLine>();
        TrendLine r = new TrendLine();
        List<Line> l = new ArrayList<Line>();
        Line lo = new Line();
        lo.setStartValue(value);
        lo.setColor("#29C3BE");
        lo.setDisplayvalue("X");
        lo.setValueOnRight("1");
        lo.setThickness("2");
        l.add(lo);
        r.setLine(l);
        rl.add(r);

        return rl;
    }

    public DatasourceDTO getDog(String sessionId) {
        chartData dog = new dogChart();

        List<TrendLine> rl = getTrendLine("240");
        return doChart(sessionId, "Dog", dog,true,rl);
    }
    public DatasourceDTO getBudget(String sessionId) {
        chartData budget = new regBudgetChart();

        List<TrendLine> rl = getTrendLine("4840");
        return doChart(sessionId, "Budget", budget,true,rl);
    }

    public DatasourceDTO getNetBudget(String sessionId) {
        chartData budget = new netBudgetChart();

        List<TrendLine> rl = getTrendLine("0");
        return doChart(sessionId, "NetBudget", budget,true,rl);
    }

    public DatasourceDTO getATM(String sessionId) {
        chartData atm = new stypeChart(4);

        List<TrendLine> rl = getTrendLine("300");
        return doChart(sessionId, "ATM", atm,true,rl);
    }

    public DatasourceDTO getPOS(String sessionId) {
        chartData pos = new stypeChart(3);

        List<TrendLine> rl = getTrendLine("1000");
        return doChart(sessionId, "POS", pos,true,rl);
    }

    public DatasourceDTO getElectric(String sessionId) {
        chartData electric = new electricChart();

        List<TrendLine> rl = getTrendLine("200");
        return doChart(sessionId, "Electric", electric,false,rl);
    }

    public DatasourceDTO getOut(String sessionId) {
        chartData out = new outChart();

        List<TrendLine> rl = getTrendLine("7965");
        return doChart(sessionId, "Out", out,true,rl );
    }

    public DatasourceDTO getIn(String sessionId) {
        chartData inc = new inChart();

        List<TrendLine> rl = getTrendLine("8100");
        return doChart(sessionId, "In", inc,true,rl );
    }

    public DatasourceDTO getInOutNet(String sessionId) {
        chartData inc = new inOutNetChart();

        List<TrendLine> rl = getTrendLine("0");
        return doChart(sessionId, "InOutNet", inc,true,rl );
    }

    public DatasourceDTO getUtilities(String sessionId) {
        chartData u = new utilitiesChart();

        List<TrendLine> rl = getTrendLine("500");
        return doChart(sessionId, "Utilities", u,false,rl );
    }

    public DatasourceDTO getMl(String sessionId) {
        chartData u = new mlChart();

        List<TrendLine> rl = getTrendLine("1000");
        return doChart(sessionId, "ML", u,false,rl );
    }

    public DatasourceDTO getUsaa(String sessionId) {
        chartData u = new usaaChart();

        List<TrendLine> rl = getTrendLine("1000");
        return doChart(sessionId, "Usaa", u,false,rl );
    }

    public DatasourceDTO getCredit(String sessionId) {
        chartData u = new creditChart();

        List<TrendLine> rl = getTrendLine("2800");
        return doChart(sessionId, "Credit", u,false,rl );
    }

    public DatasourceDTO getCapone(String sessionId) {
        chartData u = new caponeChart();

        List<TrendLine> rl = getTrendLine("1000");
        return doChart(sessionId, "CapOne", u,false,rl );
    }

    public DatasourceDTO getAmazon(String sessionId) {
        chartData u = new amazonChart();

        List<TrendLine> rl = getTrendLine("400");
        return doChart(sessionId, "Amazon", u,false,rl );
    }

    public DatasourceDTO getAaa(String sessionId) {
        chartData u = new aaaChart();

        List<TrendLine> rl = getTrendLine("350");
        return doChart(sessionId, "Aaa", u,false,rl );
    }

    public DatasourceDTO doChart(String sessionId, String label, chartData chartI, boolean bundle, List<TrendLine> r) {
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

        String minValueStr = null;
        String maxValueStr = null;
        double min = 0;
        double max = 0;

        LData ld = new LData(repository);
        List<Ledger> d = ld.filterByDate(filter, null, null);
        if (bundle) {
            ld.filterBundle(d);
        }
        List<Ledger> data = chartI.getChartData(d);
        StartStop dates = ld.getDates();

        List<Lvd> ldata = new Vector<Lvd>();
        ChartUI bobj = new ChartUI(chartI);
        bobj.go(filter, dates, data, ldata);

        boolean init = false;
        for (Lvd l : ldata) {
            if (l.getLabel().equals("Total"))
                continue;
            double v = l.getValue().doubleValue();
            Double dobj = chartI.getNetMod();
            if (dobj != null) {
                v = v - dobj.doubleValue();
                l.setValue(Double.valueOf(v));
            }
        }

        for (Lvd l : ldata) {
            if (l.getLabel().equals("Total"))
                continue;

            double v = l.getValue().doubleValue();
            if (!init) {
                init = true;
                min = v;
                max = v;
            } else {
                if (v < min)
                    min = v;
                if (v > max)
                    max = v;
            }

        }
        if (r != null) {
            TrendLine t = r.get(0);
            Line l = t.getline().get(0);
            String sv = l.getStartValue();
            double v = Double.valueOf(sv).doubleValue();
           
            if (v < min)
                min = v;
            if (v > max)
                max = v;
        }

        minValueStr = String.valueOf(min);
        maxValueStr = String.valueOf(max);

        System.out.println("MIN: " + minValueStr + " MAX: " + maxValueStr);
        DatasourceDTO ret = new DatasourceDTO();
        Chart c = new Chart();

        c.setTheme("fusion");
        c.setCaption(label);
        c.setXAxisName("Date");
        c.setYAxisName("Amount");

        c.setYAxisMinValue(minValueStr);
        c.setYAxisMaxValue(maxValueStr);

        ret.setChart(c);
        if (ldata.size() > 0)
            ldata.remove(ldata.size()-1);
        ret.setData(ldata);

        if (r != null)
            ret.setTrendlines(r);

        return ret;
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

        List<Lvd> ldata = new Vector<Lvd>();
        ChartUI bobj = new ChartUI(null);
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

        List<Lvd> ldata = new Vector<Lvd>();
        ChartUI bobj = new ChartUI(null);
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
