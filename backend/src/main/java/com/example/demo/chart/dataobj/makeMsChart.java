package com.example.demo.chart.dataobj;

import com.example.demo.bean.AllP;
import com.example.demo.bean.ChartMs;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.DatasourceMsDTO;
import com.example.demo.reports.GReport;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.state.WhichDate;
import com.example.demo.utils.mydate.DUtil;

import java.time.LocalDate;
import java.util.*;
import java.text.SimpleDateFormat;

public class makeMsChart {
    private final SessionDTO session;

    private final LedgerRepository lrepo;
    private final DatasourceMsDTO robj;

    private String cname;

    private String yname;

    private String suffix;

    private GReport gr;

    private HashMap<Integer, AllP> hmap;

    public makeMsChart(SessionDTO session, LedgerRepository r, DatasourceMsDTO obj) {
        this.session = session;
        this.lrepo = r;
        this.robj = obj;
        this.cname = "";
        this.yname = "";
        this.suffix = "";
        this.gr = new GReport(r);
        hmap = new HashMap<Integer, AllP>();
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

    public void initGr(int year) {
        SessionDTO fakeSession = new SessionDTO();

        SimpleDateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");
        String dstrs = "01/01/" + String.valueOf(year);
        String dstre = "12/31/" + String.valueOf(year);
        LocalDate ds = DUtil.getStdDate(dstrs);
        LocalDate de = DUtil.getStdDate(dstre);

        fakeSession.setWhichDate(WhichDate.BOTH);
        fakeSession.setStart(ds);
        fakeSession.setStop(de);

        fakeSession.setConsolidate(Consolidate.MONTHLY);
        try {
            gr.go(null,fakeSession);
        } catch (Exception ex) {
            return;
        }
        hmap.putIfAbsent(year,gr.getAllData());
    }

    private void setCategories() {
        List<Categories> lcc = new ArrayList<Categories>();
        Categories cat = new Categories();
        lcc.add(cat);

        List<category> lc = new ArrayList<category>();
        cat.setCategory(lc);

        int y;
        for (y = 2012; y < 2024; y++) {
            initGr(y);
            category c = new category();
            c.setLabel(String.valueOf(y));
            lc.add(c);
        }

        this.robj.setCategories(lcc);
    }

    private Dataset makeDatasetAtm() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Atm");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getAtm());
                cv1.add(cv);
            }
        }

        return cds1;
    }
    private Dataset makeDatasetPos() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Pos");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 3;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getPos());
                cv1.add(cv);
            }
        }

        return cds1;
    }

    private Dataset makeDatasetUtils() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Utils");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 6;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getUtils());
                cv1.add(cv);
            }
        }

        return cds1;
    }

    private Dataset makeDatasetCredit() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Credit");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 1;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getCredit());
                cv1.add(cv);
            }
        }

        return cds1;
    }

    private Dataset makeDatasetBills() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Bills");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 11;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getBills());
                cv1.add(cv);
            }
        }

        return cds1;
    }
    private Dataset makeDatasetSears() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Sears");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 2;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getSears());
                cv1.add(cv);
            }
        }

        return cds1;
    }

    private Dataset makeDatasetMisc() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Misc");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 9;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getMisc());
                cv1.add(cv);
            }
        }

        return cds1;
    }
    private Dataset makeDatasetMiscC() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("MiscC");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 13;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getMiscC());
                cv1.add(cv);
            }
        }

        return cds1;
    }
    private Dataset makeDatasetAnnual() {
        Dataset cds1 = new Dataset();

        cds1.setSeriesName("Annual");
        List<chartValue> cv1 = cds1.getData();
        cds1.setData(cv1);

        int p = 8;
        int y;
        for (y = 2012; y < 2024; y++) {
            AllP obj = hmap.get(y);
            if (obj != null) {
                chartValue cv = new chartValue();
                cv.setValue(obj.getAnnual());
                cv1.add(cv);
            }
        }

        return cds1;
    }

    private void setDataset() {
        List<Dataset> cds = new ArrayList<Dataset>();
        this.robj.setDataset(cds);

        Dataset atm = makeDatasetAtm();
        cds.add(atm);

        Dataset pos = makeDatasetPos();
        cds.add(pos);

        Dataset util = makeDatasetUtils();
        cds.add(util);

        Dataset credit = makeDatasetCredit();
        cds.add(credit);

        Dataset bills = makeDatasetBills();
        cds.add(bills);

        Dataset sears = makeDatasetSears();
        cds.add(sears);

        Dataset misc = makeDatasetMisc();
        cds.add(misc);

        Dataset miscC = makeDatasetMiscC();
        cds.add(miscC);

        Dataset annual = makeDatasetAnnual();
        cds.add(annual);
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
