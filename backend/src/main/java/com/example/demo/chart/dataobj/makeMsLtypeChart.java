package com.example.demo.chart.dataobj;

import com.example.demo.bean.AllMsP;
import com.example.demo.bean.AllP;
import com.example.demo.bean.ChartMs;
import com.example.demo.bean.Lvd;
import com.example.demo.chart.chartData;
import com.example.demo.chart.lype.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.DatasourceMsDTO;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.uidata.ChartUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class makeMsLtypeChart {
    private final SessionDTO session;

    private final LedgerRepository lrepo;
    private final DatasourceMsDTO robj;

    private String cname;

    private String yname;

    private String suffix;

    private List<String> clabels = null;

    public makeMsLtypeChart(SessionDTO session, LedgerRepository r, DatasourceMsDTO obj) {
        this.clabels = new ArrayList<String>();
        this.session = session;
        this.lrepo = r;
        this.robj = obj;
        this.cname = "";
        this.yname = "";
        this.suffix = "";
    }

    public void setCname(String name) {
        this.cname = name;
    }

    public void setYname(String name) {
        this.yname = name;
    }

    public void setSuffix(String s) {
        this.suffix = s;
    }
    public void go() {
        setChart();
        setCategories();
        setDataset();
    }

    private void doC(chartData chartI, List<String> labels)
    {
        List<Lvd> ldata = new Vector<>();
        ChartUI bobj = new ChartUI(chartI);
        bobj.go(this.session, chartI.getIData(), ldata);

        for (Lvd l : ldata) {
            if ((!labels.contains(l.getLabel())) && (!l.getLabel().equals("Total"))){
                labels.add(l.getLabel());
            }
        }
    }

    private void setCategories() {
        List<Categories> lcc = new ArrayList<Categories>();
        Categories cat = new Categories();
        lcc.add(cat);

        List<category> lc = new ArrayList<category>();
        cat.setCategory(lc);

        doC(new mainChart(this.session.getSession(),this.lrepo),clabels);
        doC(new mortgChart(this.session.getSession(),this.lrepo),clabels);
        doC(new slushChart(this.session.getSession(),this.lrepo),clabels);
        doC(new annualChart(this.session.getSession(),this.lrepo),clabels);
        doC(new mlChart(this.session.getSession(),this.lrepo), clabels);

        for (String label : this.clabels) {
            category c = new category();
            c.setLabel(label);
            lc.add(c);
        }

        this.robj.setCategories(lcc);
    }

    private Dataset makeMainDataset() {
        chartData chartI = new mainChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("Main", chartI);
    }

    private Dataset makeMortgDataset() {
        chartData chartI = new mortgChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("Mortg", chartI);
    }

    private Dataset makeSlushDataset() {
        chartData chartI = new slushChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("Slush", chartI);
    }

    private Dataset makeAnnualDataset() {
        chartData chartI = new annualChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("Annual", chartI);
    }

    private Dataset makeMerrillDataset() {
        chartData chartI = new mlChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("ML", chartI);
    }

    private Dataset makeWorklineDataset() {
        chartData chartI = new InChart(this.session.getSession(),this.lrepo);
        return makeGenericDataset("In", chartI,true);

    }
    private Dataset makeGenericDataset(String name, chartData chartI) {
        return makeGenericDataset(name, chartI, false);
    }
    private Dataset makeGenericDataset(String name, chartData chartI, boolean flip) {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName(name);
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);


        List<Lvd> ldata = new Vector<>();
        ChartUI bobj = new ChartUI(chartI);
        bobj.go(this.session, chartI.getIData(), ldata);

        boolean found;
        for (String label : this.clabels) {
            found = false;
            for (Lvd l : ldata) {
                if (l.getLabel().equals(label)) {
                    chartValue cv = new chartValue();
                    double v = l.getValue();
                    if (v > 70000) {
                        v = 70000;
                    }
                    if (flip) {
                        v = v * (-1);
                    }
                    cv.setValue(v);
                    cv1.add(cv);
                    found = true;
                }
            }
            if (!found) {
                chartValue cv = new chartValue();
                cv.setValue(0);
                cv1.add(cv);
            }
        }

        return cds1;
    }



    private void setDataset() {
        List<Dataset> cds = new ArrayList<Dataset>();
        this.robj.setDataset(cds);

        Dataset main = makeMainDataset();
        cds.add(main);

        Dataset mortg = makeMortgDataset();
        cds.add(mortg);

        Dataset slush = makeSlushDataset();
        cds.add(slush);

        Dataset annual = makeAnnualDataset();
        cds.add(annual);

        Dataset merrill = makeMerrillDataset();
        cds.add(merrill);

        Dataset workline = makeWorklineDataset();
        cds.add(workline);

    }

    private void setChart() {
        ChartMs c = new ChartMs();

        c.setTheme("fusion");
        c.setCaption(this.cname);
        c.setYaxisname(this.yname);
        c.setSubcaption("");
        c.setShowhovereffect("1");
        c.setNumbersuffix(this.suffix);
        c.setDrawcrossline("1");
        c.setPlottooltext("");

        this.robj.setChart(c);
    }
}
