package com.example.demo.actions;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.BudgetValuesRepository;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.utils.Utils;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class BudgetSetAction extends BaseAction implements ActionI {
    private List<List<Ledger>> ldata;
    private BudgetValuesRepository brepo;
    private LedgerRepository lrepo;
    private StypeRepository strepo;
    public BudgetSetAction(Repos r) {
        super(r);

        this.brepo = r.getBudgetValuesRepository();
        this.lrepo = r.getLedgerRepository();
        this.strepo = r.getStypeRepository();
        this.ldata = new ArrayList<List<Ledger>>();
    }

    public List<List<Ledger>> getLdata() { return this.ldata; }

    @Override
    public boolean go(SessionDTO session) throws Exception {
        LocalDate start = session.getStart();
        LocalDate stop = session.getStop();
        StartStop ds = new StartStop(start,stop);

        List<Statements> stmts = repos.getStatementsRepository().findAllByStmtdateBetween(start,stop);
        if (stmts.size() != 1) {
            System.out.println("Bad stmts.");
            return false;
        }
        return doBudgets(stmts.get(0),ds);
    }

    public boolean doBudgets(Statements stmt, StartStop ds) {
        doStype(stmt,ds, "Pos", "POS");
        doStype(stmt,ds, "Atm", "ATM");
        doUtils(stmt,ds);
        doEmma(stmt,ds);
        doLabel(stmt,ds, "Dog", 13137);
        doLabel(stmt,ds, "Life", 11451);
        doLabel(stmt,ds, "Mortgage", 12712);
        doLabel(stmt,ds, "Usaa", 11209);
        doLabel(stmt,ds, "Capone", 10264);
        doLabel(stmt,ds, "Amazon", 10019);
        doLabel(stmt,ds, "Aaa", 12933);

        doTotal(stmt,ds);
        return true;
    }

    private boolean doTotal(Statements stmt, StartStop ds) {
        Budgetvalues vb = getBv(stmt, "Total", ds);
        if (vb == null) {
            return true;
        }
        List<Budgets> data = repos.getBudgetsRepository().findAllByStmts(stmt);
        double amt = 0;
        for (Budgets b : data) {
            amt += b.getValue();
        }
        amt = Utils.convertDouble(amt);
        makeBudgetO(amt, vb, stmt, ds);
        return true;
    }

    private boolean doLabel(Statements stmt, StartStop ds, String lstr, int id) {
        Label lbl = repos.getLabelRepository().findById(id);
        List<Ledger> data = repos.getLedgerRepository().findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(),lbl);
        ldata.add(data);

        Budgetvalues vb = getBv(stmt, lstr, ds);
        if (vb == null) {
            return true;
        }
        makeBudget( data, vb, stmt,ds);
        return true;
    }

    private boolean doEmma(Statements stmt, StartStop ds) {
        Stype stype = strepo.findByName("Emma");
        List<Ledger> data = lrepo.findAllByTransdateBetweenAndStypeOrderByTransdateAsc(ds.getStart(),ds.getStop(),stype);
        List<Ledger> death = new ArrayList<Ledger>();
        for (Ledger d : data) {
            if (d.getAmount() > 0)
                death.add(d);
        }
        data.removeAll(death);
        ldata.add(data);

        Budgetvalues vb = getBv(stmt, "Emma", ds);
        if (vb == null) {
            return true;
        }
        makeBudget( data, vb, stmt,ds);
        return true;
    }

    private boolean doUtils(Statements stmt, StartStop ds) {
        Label lbl = repos.getLabelRepository().findById(10344);
        List<Ledger> data = repos.getLedgerRepository().findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(),lbl);
        ldata.add(data);

        Budgetvalues vb = getBv(stmt, "Utils", ds);
        if (vb == null) {
            return true;
        }
        makeBudget( data, vb, stmt,ds);
        return true;
    }

    private void makeBudget(List<Ledger> data, Budgetvalues vb, Statements stmt, StartStop ds) {
        double amt = 0;
        for (Ledger l : data) {
            amt += l.getAmount();
        }
        amt = Utils.convertDouble(amt);
        makeBudgetO(amt, vb, stmt, ds);
    }
    private void makeBudgetO(double amt, Budgetvalues vb, Statements stmt, StartStop ds) {
        double amtabs = Math.abs(amt);
        double net = Utils.convertDouble(amtabs - vb.getValue());

        Budgets obj = new Budgets();
        obj.setBdate(ds.getStart());
        obj.setBid(vb);
        obj.setValue(amt);
        obj.setCreated(LocalDate.now());
        obj.setNet(net);
        obj.setLastUsed(LocalDate.now());
        obj.setStmts(stmt);

        repos.getBudgetsRepository().save(obj);
        System.out.println("Ok.");
    }

    private Budgetvalues getBv(Statements stmt, String name, StartStop ds) {
        Budgetvalues vb = brepo.findByName(name);
        List<Budgets> bl = repos.getBudgetsRepository().findAllByBdateBetweenAndBidOrderByBdateAsc(ds.getStart(), ds.getStop(), vb);
        if (!bl.isEmpty()) {
            System.out.println("Already have " + name + " for " + stmt.getName());
            return null;
        }
        return vb;
    }
    private boolean doStype(Statements stmt, StartStop ds, String stb, String sty) {
        Stype stype = strepo.findByName(sty);
        List<Ledger> data = lrepo.findAllByTransdateBetweenAndStypeOrderByTransdateAsc(ds.getStart(),ds.getStop(),stype);
        ldata.add(data);

        Budgetvalues vb = getBv(stmt, stb, ds);
        if (vb == null) {
            return true;
        }

        makeBudget( data, vb, stmt,ds);
        return true;
    }
}
