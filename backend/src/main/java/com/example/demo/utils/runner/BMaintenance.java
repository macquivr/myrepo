package com.example.demo.utils.runner;

import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.repository.*;
import com.example.demo.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class BMaintenance {
    private final Repos repos;

    private Statements stmts;
    private HashMap<String, Budgetvalues> bvalues;

    private LocalDate theDate;

    private List<Ledger> mainLedger;
    private List<Ledger> usaaLedger;
    private List<Ledger> caponeLedger;

    private List<Ledger> amazonLedger;

    private List<Ledger> aaaLedger;
    private double total;
    private double totals;

    public BMaintenance(Repos r, SessionDTO session) {
        mainLedger = new ArrayList<Ledger>();
        usaaLedger = new ArrayList<Ledger>();
        caponeLedger = new ArrayList<Ledger>();
        amazonLedger = new ArrayList<Ledger>();
        aaaLedger = new ArrayList<Ledger>();

        repos = r;
        StatementsRepository stmtsRepo = repos.getStatementsRepository();
        this.stmts =  stmtsRepo.findByStmtdate(session.getStart());
        getMainData();
        getMapV();
        this.theDate = stmts.getStmtdate();
    }

    public boolean  go()
    {
        boolean b = doBudgetS();
        if (!b)
            return b;
        return doBudget();
    }

    private boolean doBudget() {
        doUtils();
        doUsaa();
        doCapone();
        doAmazon();
        doAaa();
        doAtm();
        doPos();
        doEmma();
        doDog();
        doTotal();
        return true;
    }

    private boolean  doBudgetS() {
        doUtilsS();
        doUsaaS();
        doCaponeS();
        doAmazonS();
        doAaaS();
        doAtmS();
        doPosS();
        doEmmaS();
        doDogS();
        doTotalS();
        return true;
    }

    private void getData(Ltype ltype, List<Ledger> ldata)
    {
        LedgerRepository lerepo = repos.getLedgerRepository();
        StatementRepository srepo = repos.getStatementRepository();

        Statement smain = srepo.findAllByStatementsAndLtype(this.stmts,ltype);
        List<Ledger> data = lerepo.findAllByStatement(smain);
        ldata.addAll(data);
    }
    private void getMainData() {
        List<Ledger> ret = new ArrayList<>();

        LtypeRepository lrepo = repos.getLtypeRepository();

        Ltype main = lrepo.findByName("Main Account");
        getData(main, mainLedger);

        Ltype usaa = lrepo.findByName("Usaa");
        getData(usaa, usaaLedger);

        Ltype capone = lrepo.findByName("CapitalOne");
        getData(capone, caponeLedger);

        Ltype amazon = lrepo.findByName("Amazon");
        getData(amazon, amazonLedger);

        Ltype aaa = lrepo.findByName("Aaa");
        getData(aaa, aaaLedger);


    }
    private Budget makeBudgetObj(double value, String bvs)  {
        Budget obj = new Budget();

        Budgetvalues bv =  this.bvalues.get(bvs);
        double bvd = bv.getValue();
        double net = Utils.convertDouble(value - bvd);

        obj.setBdate(this.theDate);
        obj.setBid(bv);
        obj.setCreated(LocalDate.now());
        obj.setStmts(this.stmts);
        obj.setNet(net);
        obj.setLastUsed(LocalDate.now());
        obj.setValue(value);

        return obj;
    }

    private Budgets makeBudgetsObj(double value, String bvs)  {
        Budgets obj = new Budgets();

        Budgetvalues bv =  this.bvalues.get(bvs);
        double bvd = bv.getValue();
        double net = Utils.convertDouble(value - bvd);

        obj.setBdate(this.theDate);
        obj.setBid(bv);
        obj.setCreated(LocalDate.now());
        obj.setStmts(this.stmts);
        obj.setNet(net);
        obj.setLastUsed(LocalDate.now());
        obj.setValue(value);

        return obj;
    }
    private double getValue(RMapI obj) {
        double ret = 0;

        if (obj != null) {
            for (Ledger l : mainLedger) {
                if (obj.apply(l)) {
                    ret += l.getAmount();
                }
            }
            ret = ret * (-1);
            this.total += ret;
        } else {
            ret = this.total;
        }
        return Utils.convertDouble(ret);
    }

    private double getsValue(RMapI obj, List<Ledger> data) {
        double ret = 0;

        if (obj != null) {
            for (Ledger l : data) {
                if (obj.apply(l)) {
                    ret += l.getAmount();
                }
            }
            if (ret < 0) {
                ret = ret * (-1);
            }
            this.totals += ret;
        } else {
            ret = this.totals;
        }
        return Utils.convertDouble(ret);
    }

    private void doGeneric(String label, RMapI rmap) {
        BudgetRepository br = repos.getBudgetRepository();

        Budget obj = makeBudgetObj(getValue(rmap), label);
        br.save(obj);
    }

    private void doGenericS(String label, RMapI rmap, List<Ledger> data) {
        BudgetsRepository br = repos.getBudgetsRepository();

        Budgets obj = makeBudgetsObj(getsValue(rmap,data), label);
        br.save(obj);
    }

    private void doUtils() {
        RMapI rmap = new RMapUtils();
        doGeneric("Utils", rmap);
    }

    private void doUtilsS() {
        RMapI rmap = new RMapUtils();
        doGenericS("Utils", rmap, mainLedger);
    }

    private void doUsaa() {
        RMapI rmap = new RMapUsaa();
        doGeneric("Usaa", rmap);
    }

    private void doUsaaS() {
        RMapI rmap = new RMapUsaa(12057);
        doGenericS("Utils", rmap, usaaLedger);
    }

    private void doCapone() {
        RMapI rmap = new RMapCapone();
        doGeneric("Capone", rmap);
    }

    private void doCaponeS() {
        RMapI rmap = new RMapCapone(10265);
        doGenericS("Capone", rmap, caponeLedger);
    }

    private void doAmazon() {
        RMapI rmap = new RMapAmazon();
        doGeneric("Amazon", rmap);
    }
    private void doAmazonS() {
        RMapI rmap = new RMapAmazon(10304);
        doGenericS("Amazon", rmap, amazonLedger);
    }

    private void doAaa() {
        RMapI rmap = new RMapAaa();
        doGeneric("Aaa", rmap);
    }

    private void doAaaS() {
        RMapI rmap = new RMapAaa(10076);
        doGenericS("Aaa", rmap, aaaLedger);
    }

    private void doAtm() {
        RMapI rmap = new RMapAtm();
        doGeneric("Atm", rmap);
    }

    private void doAtmS() {
        RMapI rmap = new RMapUtils();
        doGenericS("Utils", rmap,mainLedger);
    }

    private void doPos() {
        RMapI rmap = new RMapPos();
        doGeneric("Pos", rmap);
    }

    private void doPosS() {
        RMapI rmap = new RMapUtils();
        doGenericS("Pos", rmap,mainLedger);
    }

    private void doEmma() {
        RMapI rmap = new RMapEmma();
        doGeneric("Emma", rmap);
    }

    private void doEmmaS() {
        RMapI rmap = new RMapUtils();
        doGenericS("Emma", rmap,mainLedger);
    }

    private void doDog() {
        RMapI rmap = new RMapDog();
        doGeneric("Dog", rmap);
    }

    private void doDogS() {
        RMapI rmap = new RMapUtils();
        doGenericS("Dog", rmap, mainLedger);
    }

    private void doTotal() {
        doGeneric("Total", null);
    }

    private void doTotalS() {
        doGenericS("Total", null, mainLedger);
    }

    private void  getMapV() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String,Budgetvalues> map = new HashMap<>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(),b);
        }
        this.bvalues = map;
    }
}
