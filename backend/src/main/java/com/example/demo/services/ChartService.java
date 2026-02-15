package com.example.demo.services;

import com.example.demo.bean.*;
import com.example.demo.chart.*;
import com.example.demo.chart.bc.MltChart;
import com.example.demo.chart.bc.csbtChart;
import com.example.demo.chart.budget.batmChart;
import com.example.demo.chart.budget.bdogChart;
import com.example.demo.chart.budget.bposChart;
import com.example.demo.chart.dataobj.*;
import com.example.demo.chart.net.*;
import com.example.demo.chart.bcredit.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.DatasourceMsDTO;
import com.example.demo.repository.*;
import com.example.demo.dto.ui.DatasourceDTO;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.ConsolidateUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.idata.LedgerIData;
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
    private UtilitiesRepository urepository;

    @Autowired
    private LedgerRepository repository;

    @Autowired
    private StypeRepository srepository;

    @Autowired
    private CategoryRepository crepository;

    @Autowired
    private CsbtRepository csbtRepository;

    @Autowired
    private MltRepository mltRepository;

    public DatasourceMsDTO getMsline(String sessionId) {
        //chartData<Utilities> electric = new electricChart(sessionId, urepository);

        return doMsChart(sessionId, "PercentAnnual", null);
    }

    public DatasourceDTO getChartStypeAtm(String sessionId) {
        return getSpecificStype(sessionId, "ATM");
    }

    public DatasourceDTO getChartStypeBills(String sessionId) {
        return getSpecificStype(sessionId, "Bills");
    }

    public DatasourceDTO getChartStypeMisc(String sessionId) {
        return getSpecificStype(sessionId, "Misc");
    }

    public DatasourceDTO getChartStypePos(String sessionId) {
        return getSpecificStype(sessionId, "POS");
    }

    public DatasourceDTO getChartStypeAnnual(String sessionId) {
        return getSpecificStype(sessionId, "Annual");
    }

    public DatasourceDTO getChartStypeCredit(String sessionId) {
        return getSpecificStype(sessionId, "Credit");
    }

     private DatasourceDTO getSpecificStype(String sessionId, String stypeName) {
         Stype stype = srepository.findByName(stypeName);
         if (stype == null) {
             logger.error("NO stype Utils");
             return new DatasourceDTO();
         }
         chartData<Ledger> pos = new stypeChart(stype.getId(), sessionId, repository);

         return doChart(sessionId, "POS", pos,null);
     }

    private String adjustTrendLine(String sessionId, int value) {
        if (sessionId == null) {
            logger.error("No Session.");
            return null;
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return null;
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return null;
        }

        if (filter.getConsolidate() == Consolidate.NONE) {
            logger.error("No consolidate.");
            return null;
        }
        //if ((filter.getConsolidate() == Consolidate.MONTHLY) || (ConsolidateUtils.isSpecificMonth(filter.getConsolidate()))){
            // just use straight value
        //}
        if ((filter.getConsolidate() == Consolidate.QUARTERLY) || (ConsolidateUtils.isSpecificQuarter(filter.getConsolidate()))) {
            value = value * 3;
        }
        if (filter.getConsolidate() == Consolidate.HALF) {
            value = value * 6;
        }
        if (filter.getConsolidate() == Consolidate.YEARLY) {
            value = value * 12;
        }
        return String.valueOf(value);
    }
    private List<TrendLine> getTrendLine(String value) {
        List<TrendLine> rl = new ArrayList<>();
        TrendLine r = new TrendLine();
        List<Line> l = new ArrayList<>();
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
        chartData<Ledger> dog = new dogChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,240);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Dog", dog,rl);
    }
    public DatasourceDTO getBudget(String sessionId) {
        chartData<Ledger> budget = new regBudgetChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,4840);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Budget", budget,rl);
    }

    public DatasourceDTO getNetBudget(String sessionId) {
        chartData<Ledger> budget = new netBudgetChart(sessionId,repository);
        String trend = adjustTrendLine(sessionId,0);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "NetBudget", budget,rl);
    }

    public DatasourceDTO getATM(String sessionId) {
        chartData<Ledger> atm = new stypeChart(4,sessionId, repository);
        String trend = adjustTrendLine(sessionId,300);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "ATM", atm,rl);
    }

    public DatasourceDTO getPOS(String sessionId) {
        chartData<Ledger> pos = new stypeChart(3, sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "POS", pos,rl);
    }

    public DatasourceDTO getCell(String sessionId) {
        chartData<Utilities> cell = new cellChart(sessionId, urepository);
        String trend = adjustTrendLine(sessionId,100);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Cell", cell,rl);
    }
    public DatasourceDTO getCable(String sessionId) {
        chartData<Utilities> cable = new cableChart(sessionId, urepository);
        String trend = adjustTrendLine(sessionId,200);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Cable", cable,rl);
    }
    public DatasourceDTO getElectric(String sessionId) {
        chartData<Utilities> electric = new electricChart(sessionId, urepository);
        String trend = adjustTrendLine(sessionId,200);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Electric", electric,rl);
    }

    public DatasourceDTO getOut(String sessionId) {
        chartData<Ledger> out = new outChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,7965);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Out", out,rl );
    }

    public DatasourceDTO getIn(String sessionId) {
        chartData<Ledger> inc = new inChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,8100);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "In", inc,rl );
    }

    public DatasourceDTO getInOutNet(String sessionId) {
        chartData<Ledger> inc = new inOutNetChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,0);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "InOutNet", inc,rl );
    }

    public DatasourceDTO getUtilities(String sessionId) {
        chartData<Utilities> u = new utilitiesChart(sessionId, urepository);
        String trend = adjustTrendLine(sessionId,500);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Utilities", u,rl);
    }

    public DatasourceDTO getMl(String sessionId) {
        chartData<Ledger> u = new mlChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "ML", u,rl );
    }

    public DatasourceDTO getBmedical(String sessionId) {
        chartData<Ledger> u = new bmedicalChart(sessionId, repository);
        //String trend = adjustTrendLine(sessionId,500);
        //List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Medical", u,null );
    }
    public DatasourceDTO getBmortgage(String sessionId) {
        chartData<Ledger> u = new bmortgageChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,3200);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Mortgage", u,rl );
    }
    public DatasourceDTO getButilities(String sessionId) {
        chartData<Ledger> u = new butilitiesChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,545);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Utilities", u,rl);
    }
    public DatasourceDTO getBusaa(String sessionId) {
        chartData<Ledger> u = new busaaChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Usaa", u,rl );
    }

    public DatasourceDTO getBaaa(String sessionId) {
        chartData<Ledger> u = new baaaChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,350);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Aaa", u,rl );
    }

    public DatasourceDTO getBsears(String sessionId) {
        chartData<Ledger> u = new bsearsChart(sessionId, repository);
        //String trend = adjustTrendLine(sessionId,350);
        //List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Sears", u,null );
    }

    public DatasourceDTO getBcapone(String sessionId) {
        chartData<Ledger> u = new bcaponeChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "CapOne", u,rl );
    }

    public DatasourceDTO getBamazon(String sessionId) {
        chartData<Ledger> u = new bamazonChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,400);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Amazon", u,rl );
    }

    public DatasourceDTO getBpos(String sessionId) {
        chartData<Ledger> u = new bposChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "POS", u,rl );
    }

    public DatasourceDTO getBatm(String sessionId) {
        chartData<Ledger> u = new batmChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,300);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "ATM", u,rl );
    }

    public DatasourceDTO getBdog(String sessionId) {
        chartData<Ledger> u = new bdogChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,160);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Dog", u,rl );
    }

    public DatasourceDTO getBall(String sessionId) {
        chartData<Ledger> u = new ballChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,2800);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "All", u,rl );
    }


    public DatasourceDTO getUsaa(String sessionId) {
        chartData<Ledger> u = new usaaChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Usaa", u,rl );
    }

    public DatasourceDTO getCredit(String sessionId) {
        chartData<Ledger> u = new creditChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,2800);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Credit", u,rl );
    }

    public DatasourceDTO getMisc(String sessionId) {
        chartData<Ledger> u = new miscChart(sessionId, repository);
        return doChart(sessionId, "Misc", u,null );
    }

    public DatasourceDTO getCapone(String sessionId) {
        chartData<Ledger> u = new caponeChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "CapOne", u,rl );
    }

    public DatasourceDTO getCaponeFiltered(String sessionId) {
        chartData<Ledger> u = new caponeFilteredChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,1000);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "CapOneFiltered", u,rl );
    }
    public DatasourceDTO getCaponeOff(String sessionId) {
        chartData<Ledger> u = new caponeOffChart(sessionId, repository);
        return doChart(sessionId, "CapOneFiltered", u,null );
    }

    public DatasourceDTO getAmazon(String sessionId) {
        chartData<Ledger> u = new amazonChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,400);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Amazon", u,rl );
    }

    public DatasourceDTO getAaa(String sessionId) {
        chartData<Ledger> u = new aaaChart(sessionId, repository);
        String trend = adjustTrendLine(sessionId,350);
        List<TrendLine> rl = getTrendLine(trend);
        return doChart(sessionId, "Aaa", u,rl );
    }

    public DatasourceDTO getAaaOff(String sessionId) {
        chartData<Ledger> u = new aaaOffChart(sessionId, repository);
        return doChart(sessionId, "Aaa", u,null );
    }

    public DatasourceDTO getCsbt(String sessionId) {
        chartData<Csbt> obj = new csbtChart(sessionId, csbtRepository);
        return doChart(sessionId, "Csbt", obj,null );
    }
    public DatasourceDTO getMlt(String sessionId) {
        chartData<Mlt> obj = new MltChart(sessionId, mltRepository);
        return doChart(sessionId, "Mlt", obj,null );
    }

    public DatasourceDTO doChart(String sessionId, String label, chartData chartI, List<TrendLine> r) {
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

        String minValueStr;
        String maxValueStr;
        double min = 0;
        double max = 0;

        List<Lvd> ldata = new Vector<>();
        ChartUI bobj = new ChartUI(chartI);
        bobj.go(filter, chartI.getIData(), ldata);

        boolean init = false;
        for (Lvd l : ldata) {
            if (l.getLabel().equals("Total"))
                continue;
            double v = l.getValue();
            Double dobj = chartI.getNetMod();
            if (dobj != null) {
                v = v - dobj;
                l.setValue(v);
            }
        }

        for (Lvd l : ldata) {
            if (l.getLabel().equals("Total"))
                continue;

            double v = l.getValue();
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
            double v = Double.parseDouble(sv);
           
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
        if (!ldata.isEmpty())
            ldata.remove(ldata.size()-1);
        ret.setData(ldata);

        if (r != null)
            ret.setTrendlines(r);

        return ret;
    }

    public DatasourceMsDTO doMsChart(String sessionId, String label, chartData chartI) {
        DatasourceMsDTO ret = new DatasourceMsDTO();
        if (sessionId == null) {
            logger.error("No Session.");
            return ret;
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return ret;
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return ret;
        }

        if (filter.getConsolidate() == Consolidate.NONE) {
            logger.info("No consolidate set.");
            return ret;
        }

        makeMsLtypeChart mobj = new makeMsLtypeChart(filter, repository,ret);

        mobj.setCname("Out All");
        mobj.setYname("Amount");
        mobj.go();

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

        List<Ledger> data = new Vector<>();
        for (Ledger l : allData) {
            if (l.getLabel().getCategory().getId() == category.getId()) {
                if (!debit && (l.getAmount() > 0))
                    data.add(l);
                if (debit && (l.getAmount() < 0))
                    data.add(l);
            }
        }

        List<Lvd> ldata = new Vector<>();
        StartStop dates = ld.getDates();

        LedgerIData lidata = new LedgerIData(data);
        lidata.setDates(dates);
        ChartUI bobj = new ChartUI(null);
        bobj.go(filter, lidata, ldata);

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
