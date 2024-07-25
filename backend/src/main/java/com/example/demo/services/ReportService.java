package com.example.demo.services;

import com.example.demo.importer.Repos;
import com.example.demo.reports.*;
import com.example.demo.repository.*;
import com.example.demo.state.Sessions;
import com.example.demo.domain.*;
import com.example.demo.bean.*;
import com.example.demo.utils.mydate.DUtil;
import com.example.demo.utils.runner.BMaintenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.*;
import com.example.demo.utils.*;

import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.Collections;
import java.io.*;

@Service
public class ReportService {
    private static final Logger logger= LoggerFactory.getLogger(ImportService.class);

    private SessionDTO session;
    private final HashMap<String,ReportI> map = new HashMap<>();

    @Autowired
    private OcRepository ocRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetsRepository budgetsRepository;

    @Autowired
    private BudgetValuesRepository budgetvaluesRepository;

    @Autowired
    private UtilitiesRepository utilRepository;

    private Repos repos = null;

    @Autowired
    private PayeeRepository payeeRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private DupsRepository dupsRepository;

    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private LtypeRepository ltypeRepository;

    @Autowired
    private StypemapRepository stypemapRepository;

    @Autowired
    private StypeRepository stypeRepository;

    @Autowired
    private CsbTypeRepository csbTypeRepository;

    @Autowired
    private MltypeRepository mltypeRepository;

    @Autowired
    private NamesRepository namesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private TLedgerRepository tledgerRepository;

    @Autowired
    private ChecksRepository checkRepository;

    @Autowired
    private PayperiodRepository payperiodRepository;

    @Autowired
    private WdatamapRepository wdatamapRepository;

    @Autowired
    private KvpRepository kvpRepository;

    @Autowired
    private GscatRepository gscatRepository;

    @Autowired
    private LmapRepository lmapRepository;

    @Autowired
    private CmapRepository cmapRepository;

    @Autowired
    private InmapRepository inmapRepository;

    private void init()
    {
        repos = new Repos(payeeRepository,
                labelRepository,
                dupsRepository,
                statementsRepository,
                statementRepository,
                ltypeRepository,
                stypemapRepository,
                stypeRepository,
                csbTypeRepository,
                mltypeRepository,
                namesRepository,
                categoryRepository,
                locationRepository,
                ledgerRepository,
                tledgerRepository,
                checkRepository,
                utilRepository,
                budgetRepository,
                budgetsRepository,
                budgetvaluesRepository,
                ocRepository);

        this.repos.setPayPeriod(this.payperiodRepository);
        this.repos.setWdatamap(this.wdatamapRepository);
        this.repos.setKvp(this.kvpRepository);

        this.repos.setGscat(this.gscatRepository);
        this.repos.setLmap(this.lmapRepository);
        this.repos.setCmap(this.cmapRepository);
        this.repos.setInmap(this.inmapRepository);

        registerReports();
    }

    private void registerReports()
    {
        map.put("DEFAULT",new DefaultReport(repos));
        map.put("MAIN",new MainReport(repos));
        map.put("RETIRE",new RetireReport(repos));
        map.put("CREDITCAT",new CreditReport(repos));
        map.put("INOUT",new InOutReport(repos));
        map.put("IN",new InReport(repos));
        map.put("OUT",new OutReport(repos));
        map.put("MISC", new MiscReport(repos));
        map.put("BREPORT",new BReport(repos));
        map.put("CREPORT",new CReport(repos));
        map.put("OTHER",new OtherReport(repos));
        map.put("GREPORT",new GReport(repos));
        map.put("PAYPERIOD",new PayPeriodReport(repos));
        map.put("SUMMARY",new SummaryReport(repos));
    }

    public StatusDTO genReport(String sessionId) {
        if (repos == null)
            init();

        session = Sessions.getObj().getSession(sessionId);

        StatusDTO ret = new StatusDTO();
        if (session == null) {
            ret.setStatus(false);
            ret.setMessage("No Session.");
            return ret;
        }
        ret.setStatus(true);

        boolean p = session.isPercent();
        if (p) {
            BMaintenance obj = new BMaintenance(repos,session);
            try {
                boolean b = obj.go();
                if (b)
                    ret.setMessage("Ran.");
                else
                    ret.setMessage("Fail.");
            } catch (Throwable ex) {
                ex.printStackTrace();
                ret.setMessage("Fail " + ex.getMessage());
            }
            return ret;
        }

        String type = session.getReportType();
        if (type == null)
            type = "DEFAULT";
        ReportI r = map.get(type);
        if (r == null) {
            ret.setStatus(false);
            ret.setMessage("Invalid report type.");
            return ret;
        }

        try {
            File f = new File("Report.csv");
            f.delete();

            FileWriter w = new FileWriter("Report.csv");

            r.go(w, session);

            w.flush();
            w.close();
        } catch (Exception ex) {
            ret.setStatus(false);
            logger.error("Exception",ex);
            ret.setMessage(ex.getMessage() == null ? ex.toString() : ex.getMessage());
            return ret;
        }
        ret.setMessage("Ok.");
        return ret;
    }

    private void gen(FileWriter w) throws Exception {
        CategoryRepository cr = repos.getCategoryRepository();
        Category transferc = cr.findByName("Transfer");
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        DataUtils du = new DataUtils(repos);
        HashMap<Lenum, Data> dmap = du.populateDmap(session,dates);

        w.write("TYPE: " + session.getReportType());
        printPeriod(w,dates);
        printGlobalStat(w,data,transferc);
        printBalances(w,dmap);
        printStat(w, dmap, transferc);
        printStypes(w, data);

        printStype("Bills",w,data);
        printUtils(w,dates);
        printStype("Credit",w,data);
        printStype("Annual",w,data);
        printStype("Misc",w,data);
        printSpent(w,dates);
        printCategories(w);
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {
        String dstart = DUtil.getDate(null,dates.getStart(),DUtil.MMDDYYYY);
        String dstop = DUtil.getDate(null,dates.getStop(),DUtil.MMDDYYYY);

        w.write(dstart + " ==> " + dstop + "\n");
    }

    private void printGlobalStat(FileWriter w, List<Ledger> data, Category transferc) throws Exception
    {
        Ion ion = InOutNet(data,transferc);
        line(w," IN: " + ion.getIn() + " OUT: " + ion.getOut() + " NET: " + ion.getNet(), null);
        w.write("\n");
    }

    private void stat(IonL idl, Ion idata, Data data, Category t, FileWriter w) throws Exception {
        line(w,data.getLabel() + " IN: " + idl.getInLabel(idata) + " OUT: " + idl.getOutLabel(idata) + " NET: " + idata.getNet(), null);
    }

    private void printStat(FileWriter w, HashMap<Lenum, Data> data, Category transferc) throws Exception
    {
        IonL idata = new IonL();

        Ion ionm = InOutNet(data.get(Lenum.MAIN).getLdata(),transferc);
        idata.add(ionm);

        Ion ionms = InOutNet(data.get(Lenum.MAINSAVE).getLdata(),transferc);
        idata.add(ionms);

        Ion ionmortg = InOutNet(data.get(Lenum.MORTG).getLdata(),transferc);
        idata.add(ionmortg);

        Ion ionslush = InOutNet(data.get(Lenum.SLUSH).getLdata(),transferc);
        idata.add(ionslush);

        Ion ionannual = InOutNet(data.get(Lenum.ANNUAL).getLdata(),transferc);
        idata.add(ionannual);

        Ion ionml = InOutNet(data.get(Lenum.ML).getLdata(),transferc);
        idata.add(ionml);

        stat(idata,ionm,data.get(Lenum.MAIN),transferc,w);
        stat(idata,ionms,data.get(Lenum.MAINSAVE),transferc,w);
        stat(idata,ionmortg,data.get(Lenum.MORTG),transferc,w);
        stat(idata,ionslush,data.get(Lenum.SLUSH),transferc,w);
        stat(idata,ionannual,data.get(Lenum.ANNUAL),transferc,w);
        stat(idata,ionml,data.get(Lenum.ML),transferc,w);

        w.write("\n");
    }

    private void line(FileWriter w, String label, Double value) throws Exception {
        if (value != null)
            w.write(label + " " + value + "\n");
        else
            w.write(label + "\n");
    }

    private double Pb(FileWriter w, Lenum e, HashMap<Lenum, Data> data,double total) throws Exception{
        Data d = data.get(e);
        if (d.getStmt() != null) {
            total += d.getStmt().getFbalance();
            line(w, d.getLabel(), d.getStmt().getFbalance());
        }
        return total;
    }
    private void printBalances(FileWriter w, HashMap<Lenum,Data> data) throws Exception {
        double total = 0.0;
        line(w, "Balances:",(double) 0);

        total = Pb(w,Lenum.MAIN,data,total);
        total = Pb(w,Lenum.MAINSAVE,data,total);
        total = Pb(w,Lenum.MORTG,data,total);
        total = Pb(w,Lenum.SLUSH,data,total);
        total = Pb(w,Lenum.ANNUAL,data,total);
        total = Pb(w,Lenum.ML,data,total);

        total = Utils.convertDouble(total);
        w.write("Total: " + total);
        w.write("\n\n");
    }

    private void printStypes(FileWriter w, List<Ledger> bundle) throws Exception {
        HashMap<Stype,Double> map = new HashMap<>();
        for (Ledger l : bundle) {
            Stype s = l.getStype();
            Double d = map.get(s);
            if (d == null)
                map.put(s,l.getAmount());
            else {
                double dv = d + l.getAmount();
                map.put(s,dv);
            }
        }
        Set<Stype> keys = map.keySet();
        for (Stype key : keys) {
            if (!key.getName().equals("Transfer")) {
                double value = Utils.convertDouble(map.get(key));
                line(w,key.getName() + "\t",value);
            }
        }
    }

    private void printSpent(FileWriter w, StartStop dates) throws Exception
    {
        w.write("\n");
        w.write("Credit spent\n");

        printSpent(w, "Usaa",  dates);
        printSpent(w, "Aaa",  dates);
        printSpent(w, "CapitalOne",  dates);
        printSpent(w, "Amazon",  dates);
    }

    private void printSpent(FileWriter w, String lt, StartStop dates) throws Exception
    {
        Ltype ltype = repos.getLtypeRepository().findByName(lt);
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,ltype);

        double total = 0;
        for (Ledger l : data) {
            if (l.getAmount() < 0)
                total += l.getAmount();
        }
        w.write(lt + ": " + Utils.convertDouble(total) + "\n");
    }

    private void printUtils(FileWriter w, StartStop dates) throws Exception
    {
        UtilitiesRepository urepo = repos.getUtilitiesRepository();
        List<Utilities> data = urepo.findAllByDateBetween(dates.getStart(),dates.getStop());
        double cell = 0;
        double cable = 0;
        double electric = 0;

        w.write("\n");
        w.write("Utilities\n");
        for (Utilities u : data) {
            cell += u.getCell();
            cable += u.getCable();
            electric += u.getElectric();
        }

        w.write("Cell: " + Utils.convertDouble(cell) + " Cable: " + Utils.convertDouble(cable) + " Electric: " + Utils.convertDouble(electric) + "\n");

    }

    private void printCategories(FileWriter w) throws Exception
    {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);

        w.write("\n");
        w.write("By Category\n");
        HashMap<String, Double> map = new HashMap<>();

        String lstr;
        for (Ledger l : data) {
            if (l.getChecks() != null) {
                Checktype ct = l.getChecks().getPayee().getCheckType();
                if ((ct.getName().equals("Annual")) ||
                        (ct.getName().equals("Bills")))
                    lstr = ct.getName();
                else
                    lstr = l.getLabel().getCategory().getName();
            }
            else
                lstr = l.getLabel().getCategory().getName();
            
            if (l.getAmount() < 0)
                lstr = "D " + lstr;
            else
                lstr = "C " + lstr;

            Double d = map.get(lstr);
            if (d == null) {
                map.put(lstr, l.getAmount());
            } else {
                d = Utils.convertDouble(d + l.getAmount());
                map.put(lstr, d);
            }
        }
        List<Catsort> sort = new Vector<>();

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String akey = Utils.getAkey(key,map);

            Double d = map.get(key);
            Catsort c = new Catsort();
            c.setLabel(akey);
            c.setAmount(d);
            sort.add(c);
        }
        Collections.sort(sort);
        for (Catsort c : sort)
            w.write(c.getLabel() + " " + c.getAmount() + "\n");
    }

    private void printStype(String stype, FileWriter w, List<Ledger> bundle) throws Exception
    {
        w.write("\n");
        w.write("Stype " + stype + "\n");
        HashMap<String, Double> map = new HashMap<>();

        String lstr;
        for (Ledger l : bundle) {
            Stype s = l.getStype();
            if (s.getName().equals(stype)) {
                if (l.getChecks() != null)
                    lstr = l.getChecks().getPayee().getName();
                else
                    lstr = l.getLabel().getNames().getName();
                Double d = map.get(lstr);
                if (d == null) {
                    map.put(lstr, l.getAmount());
                } else {
                    double dv = Utils.convertDouble(d + l.getAmount());
                    map.put(lstr, dv);
                }
            }
        }
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Double d = map.get(key);
            w.write(key + " " + d + "\n");
        }
    }

    private Ion InOutNet(List<Ledger> data, Category transferc) {
        Ion ret = new Ion();

        for (Ledger l : data) {
            Label lbl = l.getLabel();
            Category c = lbl.getCategory();
            if (c.getId() != transferc.getId()) {
                if (l.getAmount() > 0)
                    ret.setIn(ret.getIn() + l.getAmount());
                if (l.getAmount() < 0)
                    ret.setOut(ret.getOut() + l.getAmount());
            }
        }
        ret.setNet(ret.getIn() + ret.getOut());

        return ret;
    }
}
