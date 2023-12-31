package com.example.demo.services;


import com.example.demo.bean.Ion;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.dto.ui.*;
import com.example.demo.dto.*;
import com.example.demo.state.Consolidate;
import com.example.demo.state.Sessions;
import com.example.demo.state.WhichDate;
import com.example.demo.utils.*;
import com.example.demo.utils.idata.*;
import com.example.demo.utils.idate.LedgerIDate;
import com.example.demo.utils.rutils.OcUtils;
import com.example.demo.utils.uidata.*;

import com.example.demo.bean.Lvd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TableDataService {
    private static final Logger logger = LoggerFactory.getLogger(TableDataService.class);

    private Repos repos = null;

    @Autowired
    private OcRepository ocRepository;

    @Autowired
    private BudgetValuesRepository budgetvaluesRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetsRepository budgetsRepository;

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
    private ChecksRepository checkRepository;

    @Autowired
    private UtilitiesRepository utilRepository;

    private void init() {
        logger.info("INITIALIZING REPO....");
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
                checkRepository,
                utilRepository,
                budgetRepository,
                budgetsRepository,
                budgetvaluesRepository,
                ocRepository);
    }

    public BVNTableDTO doBnet(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        BNIData ldata = new BNIData(bndata);
        ldata.setDates(dates);
        BNUI bobj = new BNUI(getMap());
        bobj.go(session, ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBsnet(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        BNSIData ldata = new BNSIData(bndata);
        ldata.setDates(dates);
        BNSUI bobj = new BNSUI(getMap());
        bobj.go(session, ldata, rdata);

        return ret;
    }

    private HashMap<String, Integer> getMap() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String, Integer> map = new HashMap<>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(), b.getId());
        }
        return map;
    }

    public BVNTableDTO doBvalues(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVIData ldata = new BVIData(bvdata);
        ldata.setDates(dates);
        BVUI bobj = new BVUI(getMap());
        bobj.go(session, ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBvs(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVSIData ldata = new BVSIData(bvdata);
        ldata.setDates(dates);
        BVSUI bobj = new BVSUI(getMap());
        bobj.go(session, ldata, rdata);

        return ret;
    }

    public InOutNetTableDTO doInOutNet(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();
        InOutNetTableDTO ret = new InOutNetTableDTO();
        List<InOutNetRowDTO> data = new Vector<>();
        ret.setInoutnet(data);

        if (session == null)
            return ret;

        populateInOutNet(data, session);

        return ret;
    }

    public CStatusTableDTO doCStatus(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        LocalDate dt = session.getStart();

        OcUtils utils = new OcUtils(this.repos);
        return utils.makeTableData(dt);
    }

    public BalanceTableDTO doBalance() {

        if (repos == null)
            init();

        List<BalanceRowDTO> data = new Vector<>();
        StatementsRepository srepo = repos.getStatementsRepository();
        List<Statements> all = srepo.findAll();
        Statements last = all.get(all.size() - 1);

        BalanceTableDTO ret = new BalanceTableDTO();
        double total = 0;
        List<Statement> stmts = last.getStatement();

        LtypeRepository ltyper = repos.getLtypeRepository();
        Ltype dmain = ltyper.findByName("Main Account");
        Ltype dmortg = ltyper.findByName("Mortgage Account");
        Ltype dms = ltyper.findByName("Main Savings");
        Ltype dslush = ltyper.findByName("SlushFund");
        Ltype dannual = ltyper.findByName("Annual Account");
        Ltype dml = ltyper.findByName("Merrill Lynch");

        for (Statement stmt : stmts) {
            if (stmt.getLtype().getId() == dmain.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dmain.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
            if (stmt.getLtype().getId() == dmortg.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dmortg.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
            if (stmt.getLtype().getId() == dms.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dms.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
            if (stmt.getLtype().getId() == dslush.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dslush.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
            if (stmt.getLtype().getId() == dannual.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dannual.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
            if (stmt.getLtype().getId() == dml.getId()) {
                BalanceRowDTO b = new BalanceRowDTO();
                b.setName(dml.getName());
                b.setAmount(stmt.getFbalance());
                total += stmt.getFbalance();
                data.add(b);
            }
        }
        total = Utils.convertDouble(total);
        BalanceRowDTO b = new BalanceRowDTO();
        b.setName("TOTAL");
        b.setAmount(total);
        data.add(b);
        ret.setBalance(data);
        return ret;
    }

    public StypeTableDTO doStype(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        StypeTableDTO ret = new StypeTableDTO();
        List<StypeRowDTO> data = new Vector<>();
        ret.setStype(data);

        if (session == null)
            return ret;

        populateStype(data, session);

        if (session.isPercent())
            ret.doPercent();

        ret.print();

        return ret;
    }

    public BillsTableDTO doBills(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        List<Ledger> data = new Vector<>();
        StartStop dates = addSType(session, "Bills", data);

        List<BillsRowDTO> bdata = new Vector<>();
        BillsTableDTO ret = new BillsTableDTO(bdata);

        LedgerIData ldata = new LedgerIData(data);
        ldata.setDates(dates);
        BillsUI bobj = new BillsUI();
        bobj.go(session,  ldata, bdata);

        return ret;
    }

    public StatementTableDTO doStatements(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        List<StatementRowDTO> rdata = new Vector<>();
        StatementTableDTO ret = new StatementTableDTO(rdata);

        if (repos == null)
            init();

        WhichDate wd = session.getWhichDate();
        if (wd == WhichDate.START) {
            populateOneStatement(ret, session);
            return ret;
        }

        int lid = session.getLtype();
        if (lid <= 0)
            return ret;

        LData ld = new LData(repos.getLedgerRepository());
        StartStop dates = ld.getDates();

        StatementsRepository repo = repos.getStatementsRepository();
        List<Statements> sdata = repo.findAllByStmtdateBetween(dates.getStart(), dates.getStop());
        List<Statement> stmts = new Vector<>();
        for (Statements st : sdata) {
            for (Statement s : st.getStatement()) {
                if (s.getLtype().getId() == lid)
                    stmts.add(s);
            }
        }
        populateStatements(stmts, ret);

        return ret;
    }

    public AnnualTableDTO doAnnual(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        List<Ledger> data = new Vector<>();
        StartStop dates = addSType(session, "Annual", data);

        List<AnnualRowDTO> adata = new Vector<>();
        AnnualTableDTO ret = new AnnualTableDTO(adata);
        AnnualUI aobj = new AnnualUI();

        LocalDate now = LocalDate.now();
        LocalDate start = dates.getStart();
        if (session.getConsolidate().equals(Consolidate.YEARLY) &&
                (start.getYear() == now.getYear())) {
            AnnualRowDTO srow = new AnnualRowDTO();
            adata.add(srow);
            for (Ledger l : data) {
                LedgerIDate ld = new LedgerIDate(l);
                aobj.apply(ld, srow);
            }
        } else {
            LedgerIData ldata = new LedgerIData(data);
            aobj.go(session, ldata, adata);
        }
        calcAnnualTotal(adata);
        return ret;
    }

    public UtilsTableDTO doUtils(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        LData ld = new LData(repos.getLedgerRepository());
        //List<Ledger> tmp = ld.filterByDate(session,null,null);
        StartStop dates = ld.getDates();

        UtilitiesRepository urepo = repos.getUtilitiesRepository();
        List<Utilities> data = urepo.findAllByDateBetween(dates.getStart(), dates.getStop());

        UtilsTableDTO ret = new UtilsTableDTO();
        List<UtilsRowDTO> rdata = new Vector<>();
        ret.setUtils(rdata);

        UtilitiesIData udata = new UtilitiesIData(data);

        UtilsUI uobj = new UtilsUI();
        uobj.go(session, udata, rdata);

        return ret;
    }

    public CreditTableDTO doCreditp(String sessionId) {
        return doCredit(sessionId, true);
    }

    public CreditTableDTO doCredits(String sessionId) {
        return doCredit(sessionId, false);
    }

    public CategoriesUIDTO getCategoriesData(String sessionId) {
        if (sessionId == null) {
            logger.error("No Session.");
            return new CategoriesUIDTO();
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return new CategoriesUIDTO();
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return new CategoriesUIDTO();
        }

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> ldata = ld.filterByDate(filter, null, null);

        List<Lvd> data = new Vector<>();
        CategoriesUIDTO ret = new CategoriesUIDTO(data);
        HashMap<String, Double> map = new HashMap<>();

        String lstr;
        for (Ledger l : ldata) {
            Label lbl = l.getLabel();
            Category c = lbl.getCategory();

            if (l.getAmount() < 0)
                lstr = "D " + c.getName();
            else {

                lstr = "C " + c.getName();
            }

            Double dv = map.get(lstr);
            if (dv == null)
                map.put(lstr, l.getAmount());
            else {
                dv = Utils.convertDouble(dv + l.getAmount());
                map.put(lstr, dv);
            }
        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            Double dv = map.get(key);
            if (dv == 0)
                continue;
            String akey = Utils.getAkey(key, map);
            Lvd d = new Lvd();
            d.setLabel(akey);
            d.setValue(map.get(key));
            data.add(d);
        }

        return ret;
    }




    private void populateStatements(List<Statement> stmts, StatementTableDTO data) {
        List<StatementRowDTO> tdata = stmts.stream().map(StatementRowDTO::new).collect(Collectors.toList());
        data.setStatement(tdata);
    }

    private void populateOneStatement(StatementTableDTO data, SessionDTO session) {
        StatementsRepository repo = repos.getStatementsRepository();
        Statements stmts = repo.findByStmtdate(session.getStart());
        populateStatements(stmts.getStatement(), data);
    }

    private StartStop pop(SessionDTO session,   List<Ledger> data,String label)
    {
        LData ld = new LData(repos.getLedgerRepository());
        Ltype ltype = repos.getLtypeRepository().findByName(label);
        List<Ledger> tmp  = ld.filterByDate(session,null,ltype);

        for (Ledger l : tmp) {
            if (l.getAmount() < 0)
                data.add(l);
        }
        return ld.getDates();
    }
    private CreditTableDTO doCredit(String sessionId, boolean paid)
    {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        StartStop dates;

        List<Ledger> data = new Vector<>();
        if (paid)
            dates = addSType(session,"Credit",data);
        else {
            dates = pop(session, data, "Usaa");
            pop(session, data, "CapitalOne");
            pop(session, data, "Aaa");
            pop(session, data, "Amazon");
        }

        CreditTableDTO ret = new CreditTableDTO();
        List<CreditRowDTO> rdata = new Vector<>();
        ret.setCredit(rdata);

        LedgerIData ldata = new LedgerIData(data);
        ldata.setDates(dates);
        CreditUI uobj = new CreditUI(paid);
        uobj.go(session, ldata,rdata);

        return ret;
    }



    private void calcAnnualTotal(List<AnnualRowDTO> data) {
        for (AnnualRowDTO d : data) {
            double total = d.getAaa() +
                    d.getBjs() +
                    d.getExcise() +
                    d.getMatax() +
                    d.getFed() +
                    d.getGenerator() +
                    d.getOil() +
                    d.getOilservice() +
                    d.getPropane() +
                    d.getRmv() +
                    d.getSecurity() +
                    d.getTaxprep() +
                    d.getCarins() +
                    d.getSafetydep() +
                    d.getEscrow() +
                    d.getTerminix() +
                    d.getAmazonprime() +
                    d.getNorton();
            d.setTotal(Utils.convertDouble(total));
        }
    }


    public NameLocCatDTO getNameLocCat(String sessionId) {
        SessionDTO session = Sessions.getObj().getSession(sessionId);

        if (repos == null)
            init();

        String state = (session == null) ? "NONE" : session.getNlc();

        List<Names> names = repos.getNamesRepository().findAll();
        List<Location> locations = repos.getLocationRepository().findAll();
        List<Category> categories = repos.getCategoryRepository().findAll();

        NameLocCatDTO ret = new NameLocCatDTO(names,locations,categories);
        ret.setState(state);

        return ret;
    }

    private StartStop addSType(SessionDTO session, String sname, List<Ledger> data) {
        StypeRepository srepo = repos.getStypeRepository();
        Stype stype = srepo.findByName(sname);
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> ldata = ld.filterByDate(session, stype, null);
        if (!sname.equals("Annual"))
            ld.filterBundle(ldata);
        data.addAll(ldata);
        return ld.getDates();
    }

    private void populateStype(List<StypeRowDTO> data, SessionDTO session) {
        List<Ledger> ldata = new Vector<>();
        StartStop dates = addSType(session, "Bills", ldata);
        addSType(session, "Annual", ldata);
        addSType(session, "ATM", ldata);
        addSType(session, "Deposit", ldata);
        addSType(session, "Misc", ldata);
        addSType(session, "POS", ldata);
        addSType(session, "Credit", ldata);

        LedgerIData lidata = new LedgerIData(data);
        lidata.setDates(dates);
        StypeUI sobj = new StypeUI();
        sobj.go(session, lidata, data);
    }

    private void populateInOutNet(List<InOutNetRowDTO> data, SessionDTO session) {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> d = null;

        int lt = session.getLtype();
        if (lt > 0) {
            Ltype ltype;
            Optional<Ltype> o = repos.getLtypeRepository().findById(lt);
            if (o.isPresent()) {
                ltype = o.get();
                d = ld.filterByDate(session, null, ltype);
            }
        } else {
            d = ld.filterByDate(session, null, null);
            ld.filterBundle(d);
        }

	    if ((d == null) || (d.isEmpty()))
	        return;
	
        CategoryRepository cr = repos.getCategoryRepository();
        Category transferc = cr.findByName("Transfer");

        List<Ion> il = InOutNet(d, transferc,  ld.getDates(), session);

        for (Ion i : il) {
            InOutNetRowDTO row = new InOutNetRowDTO();
            row.setLabel(i.getLabel());
            row.setInAmt(i.getIn());
            row.setOutAmt(i.getOut());
            row.setNet(i.getNet());
            data.add(row);
        }
    }

    private List<Ion> InOutNet(List<Ledger> data, Category transferc,  StartStop dates, SessionDTO session) {
        List<Ion> lst = new Vector<>();

        LedgerIData ldata = new LedgerIData(data);
        ldata.setDates(dates);
        InOutNetUI obj = new InOutNetUI(transferc);
        obj.go(session,ldata,lst);

        return lst;
    }
}
