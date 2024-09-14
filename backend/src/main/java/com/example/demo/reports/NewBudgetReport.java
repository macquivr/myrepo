package com.example.demo.reports;

import com.example.demo.actions.BudgetSetAction;
import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NewBudgetReport implements ReportI {
    private final Repos repos;
    private MRBeanl bdata;
    private MRBeanl cdata;

    public NewBudgetReport(Repos r) {
        this.repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public String go(FileWriter w, SessionDTO session) throws Exception {
        bdata = new MRBeanl();
        cdata = new MRBeanl();

        List<Statements> stmts = repos.getStatementsRepository().findAllByStmtdateBetween(session.getStart(), session.getStop());
        if (stmts.size() != 1) {
            return "Bad stmts.";
        }
        Statements s = stmts.get(0);
        List<Budgets> bobjs = repos.getBudgetsRepository().findAllByStmts(s);
        for (Budgets bo : bobjs) {
            if (!bo.getBid().getName().equals("Total")) {
                MRBean b = new MRBean(bo.getBid().getName(), bo.getValue(), bo.getBid().getValue(), bo.getNet());
                bdata.add(b);
            }
        }
        double bto = bdata.Print(w);

        StartStop ds = new StartStop(session.getStart(), session.getStop());
        rest(w, s, ds, bdata.getTotalB(), bto);
        return null;
    }

    private List<Ledger> filter(List<Ledger> data) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : data) {
            if ((l.getLtype().getId() == 3) ||
                    (l.getLtype().getId() == 5) ||
                    (l.getLtype().getId() == 6) ||
                    (l.getLtype().getId() == 11) ||
                    (l.getLtype().getId() == 12) ||
                    (l.getLtype().getId() == 14)) {
                ret.add(l);
            }
        }
        return ret;
    }
    private void rest(FileWriter w, Statements s, StartStop ds, double totalb, double bto) throws Exception {
        double workIn = 0;
        double nonWorkIn = 0;
        double totalOut = 0;

        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Ledger> credita = lrepo.findAllByTransdateBetweenAndAmountGreaterThanOrderByTransdateAsc(ds.getStart(), ds.getStop(), 0);
        List<Ledger> debta = lrepo.findAllByTransdateBetweenAndAmountLessThanOrderByTransdateAsc(ds.getStart(), ds.getStop(), 0);
        List<Ledger> credit = filter(credita);
        List<Ledger> debt = filter(debta);

        for (Ledger l : debt) {
            if (l.getStype().getId() != 8) {
                totalOut += l.getAmount();
            }
        }
        totalOut = Utils.convertDouble(totalOut);

        for (Ledger l : credit) {
            if (l.getLabel().getId() == 12448)
                workIn += l.getAmount();
            else {
                if (l.getStype().getId() != 8) {
                    nonWorkIn += l.getAmount();
                }
            }
        }

        BudgetSetAction bsa = new BudgetSetAction(repos);
        bsa.doBudgets(s, ds);

        List<List<Ledger>> ldata = bsa.getLdata();
        for (List<Ledger> lst : ldata) {
            debt.removeAll(lst);
        }

        double tin = Utils.convertDouble(workIn + nonWorkIn);
        double bt = Utils.convertDouble(totalb);
        w.write("\nActual In: " + tin + "\n");
        double adjustment = Utils.convertDouble(tin - bt);
        w.write("Adjustment: " + adjustment + "\n");
        w.write("  Work: " + workIn + "\n");
        w.write("  OtherIn: " + Utils.convertDouble(nonWorkIn) + "\n");

        w.write("\n");
        w.write("Total Out: " + totalOut + "\n");
        w.write("Actual Net: " + Utils.convertDouble(tin + totalOut) + "\n");

        double bills = 0;
        double other = 0;
        double annual = 0;

        if (!debt.isEmpty()) {
            w.write("\n");
            annual = doAnnual(w, debt);
        }

        if (!debt.isEmpty()) {
            w.write("\n");
            bills = doBills(w, debt);
        }

        if (!debt.isEmpty()) {
            w.write("Misc unbudgeted:\n");
            other = pOut(w,debt);
            w.write("Total: " + Utils.convertDouble(other) + "\n");
        }
        double o = Utils.convertDouble(bills + annual + other);
        double t = Utils.convertDouble(o + bto);
        w.write("O: " + o + " bto: " + bto + " T: " + t + "\n");
    }

    private double doBills(FileWriter w, List<Ledger> debt) throws Exception {
        List<Ledger> ad = new ArrayList<Ledger>();
        double bills = 0;
        for (Ledger a : debt) {
            if (a.getStype().getId() == 2) {
                ad.add(a);
                bills += a.getAmount();
            }
        }
        if (!ad.isEmpty()) {
            debt.removeAll(ad);
            w.write("Other Bills: " + Utils.convertDouble(bills) + "\n");
        }
        return bills;
    }

    private double doAnnual(FileWriter w, List<Ledger> debt) throws Exception {
        List<Ledger> ad = new ArrayList<Ledger>();
        double annual = 0;
        for (Ledger a : debt) {
            if (a.getStype().getId() == 6) {
                ad.add(a);
                annual += a.getAmount();
            }
        }
        debt.removeAll(ad);
        w.write("Annual: " + Utils.convertDouble(annual) + "\n");
        return annual;
    }

    private void pIn(List<Ledger> credit) {
        for (Ledger l : credit) {
            System.out.println(l.getTransdate().toString() + " " + l.getLabel().getName() + " " + l.getAmount());
        }
    }

    private double pOut(FileWriter w, List<Ledger> debt) throws Exception {
        double ret = 0;
        for (Ledger l : debt) {
            if (l.getStype().getId() != 8) {
                w.write(l.getLtype().getId() + " " + l.getTransdate().toString() + " " + l.getLabel().getName() + " " + l.getAmount() + "\n");
                ret += l.getAmount();
            }
        }
        return Utils.convertDouble(ret);
    }
    private String doBudgetObject(FileWriter w, Statements stmt, String name) {
        String label = "";
        double value = 0;
        double budget = 0;
        double net = 0;

        Budgetvalues vb = repos.getBudgetValuesRepository().findByName(name);
        if (vb == null) {
            return "No Vb.";
        }
        Budgets bobj = repos.getBudgetsRepository().findByStmtsAndBid(stmt, vb);
        if (bobj == null) {
            return "No budget object.";
        }

        MRBean b = new MRBean(vb.getName(), bobj.getValue(), vb.getValue(), bobj.getNet());
        bdata.add(b);

        return null;
    }

    /*******************************************/

    private List<Ledger> getBillsNoUtils(List<Ledger> data) {
        List<Ledger> r = new ArrayList<>();

        for (Ledger l : data) {
            if ((l.getStype().getName().equals("Bills")) &&
                    (l.getLabel().getId() != 13137) &&
                    (l.getLabel().getId() != 10344)) {
                r.add(l);
            }
        }
        return r;
    }
    private double getMiscOut(List<Ledger> data) {
        double ret = 0;

        for (Ledger l : data) {
            if ((l.getStype().getName().equals("Misc")) &&
                    (l.getAmount() < 0)) {
                ret += l.getAmount();
            }
        }
        return Utils.convertDouble(ret);
    }
    private double getIn(List<Ledger> data) {
        double ret = 0;

        for (Ledger l : data) {
            if ((l.getAmount() > 0) && (l.getStype().getId() != 8))
                ret += l.getAmount();
        }
        return Utils.convertDouble(ret);
    }


    public String go2(FileWriter w, SessionDTO session) throws Exception
    {
        bdata = new MRBeanl();
        cdata = new MRBeanl();

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);
        double camt;
        double slushCredit = 0;

        Consolidate c = session.getConsolidate();


        int m = 1;
        if (c == Consolidate.QUARTERLY) {
            m = 3;
        }
        if (c == Consolidate.HALF) {
            m = 6;
        }
        if (c == Consolidate.YEARLY) {
            m = 12;
        }

        double posBudget = 1000 * m;
        double atmBudget = 300 * m;
        double dogBudget = 280 * m;
        double usaaBudget = 1000 * m;
        double aaaBudget = 400 * m;
        double capOneBudget = 1000 * m;
        double amazonBudget = 400 * m;
        double utilBudget = 500 * m;
        double emmaBudget = 300 * m;
        double ocheck = 0;

        printStype("Emma",data,emmaBudget);
        printStype("POS",data, posBudget);
        printStype("ATM",data, atmBudget);
        double dogAmt = dog(data,dogBudget);
        slushCredit += printCredit(data,"Usaa",  11209, usaaBudget);
        slushCredit += printCredit(data,"Aaa",   12933,aaaBudget);
        slushCredit += printCredit(data,"CapitalOne", 10264,  capOneBudget);
        slushCredit += printCredit(data,"Amazon",   10019, amazonBudget);
        printCredit(data,"Utilities", 10344, utilBudget);
        ocheck = bdata.Print(w);
        w.write("\n");

        ocheck += slushCredit;
        w.write("Credit Overage: " + Utils.convertDouble(slushCredit) + "\n");

        double otherBills = 0;
        List<Ledger> obills = getBillsNoUtils(data);
        for (Ledger l : obills) {
            otherBills = Utils.convertDouble(otherBills + l.getAmount());
        }
        w.write("OtherBills: " + otherBills + "\n");
        ocheck += otherBills;

        double miscNoDog = Utils.convertDouble(getMiscOut(data) - dogMisc(data));
        w.write("Misc: " + miscNoDog + "\n");
        ocheck += miscNoDog;

        double annual = getStype(data, "Annual");

        w.write("Annual: " + annual + "\n");
        ocheck += annual;

        camt = allCredit(data);
        camt = Utils.convertDouble(camt);

        double otherCredit = Utils.convertDouble(getStype(data, "Credit") + (camt * (-1)));
        w.write("Other Credit: " + otherCredit + "\n");
        ocheck += otherCredit;

        double totalOut = getTotalOut(data);
        w.write("TotalOut: " + totalOut + " " + ocheck + "\n");

        double totalIn = getIn(data);
        w.write("TotalIn: " + totalIn + "\n");

        w.write("Net: " + Utils.convertDouble(totalIn + totalOut));

        w.write("\n");
        w.write("\n");

        printOBills(w,obills);
        w.write("\n");
        w.write("\n");

        printMisc(w,data);
        w.write("\n");
        w.write("\n");

        w.write("Credit spent calendar based.\n");
        printSpent("Usaa",  session, 1000);
        printSpent( "Aaa",  session, 400);
        printSpent( "CapitalOne",  session, 1000);
        printSpent( "Amazon",  session, 400);
        w.write("\n");
        w.write("\n");
        cdata.Print(w);

        return null;
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

    private void printOBills(FileWriter w, List<Ledger> data) throws Exception
    {
        w.write("Other Bills...\n");
        HashMap<String, Double> m = new HashMap<>();
        for (Ledger l : data) {
            if (l.getChecks() != null) {
                Double d = m.get(l.getChecks().getPayee().getName());
                if (d == null) {
                    m.put(l.getChecks().getPayee().getName(), l.getAmount());
                } else {
                    Double nv = Utils.convertDouble(d + l.getAmount());
                    m.put(l.getChecks().getPayee().getName(), nv);
                }
            } else {
                Double d = m.get(l.getLabel().getName());
                if (d == null) {
                    m.put(l.getLabel().getName(),l.getAmount());
                } else {
                    Double nv = Utils.convertDouble(d + l.getAmount());
                    m.put(l.getLabel().getName(),nv);
                }
            }
        }
        Set<String> keys = m.keySet();
        for (String key : keys) {
            Double value = m.get(key);
            if (value < 0) {
                w.write(key + " " + value + "\n");
            }
        }
        /* nop */
    }

    private void printMisc(FileWriter w, List<Ledger> data) throws Exception
    {
        w.write("Misc Entries...\n");
        HashMap<String, Double> m = new HashMap<>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Misc") && !isDog(l)) {
                if (l.getChecks() != null) {
                    Double d = m.get(l.getChecks().getPayee().getName());
                    if (d == null) {
                        m.put(l.getChecks().getPayee().getName(), l.getAmount());
                    } else {
                        Double nv = Utils.convertDouble(d + l.getAmount());
                        m.put(l.getChecks().getPayee().getName(),nv);
                    }
                    //w.write(l.getChecks().getPayee().getName() + " " + l.getAmount() + "\n");
                } else {
                    Double d = m.get(l.getLabel().getName());
                    if (d == null) {
                        m.put(l.getLabel().getName(),l.getAmount());
                    } else {
                        Double nv = Utils.convertDouble(d + l.getAmount());
                        m.put(l.getLabel().getName(),nv);
                    }
                    //w.write(l.getLabel().getName() + " " + l.getAmount() + "\n");
                }
            }
        }
        Set<String> keys = m.keySet();
        for (String key : keys) {
            Double value = m.get(key);
            if (value < 0) {
                w.write(key + " " + value + "\n");
            }
        }
        w.write("\nMiscCredit....\n");

        keys = m.keySet();
        for (String key : keys) {
            Double value = m.get(key);
            if (value > 0) {
                w.write(key + " " + value + "\n");
            }
        }
    }
    private double getTotalOut(List<Ledger> data) {
        double ret = 0;
        for (Ledger l : data) {
            if ((l.getAmount() < 0) && (!l.getStype().getName().equals("Transfer"))) {
                ret += l.getAmount();
            }
        }
        return Utils.convertDouble(ret);
    }
    private double getStype(List<Ledger> data, String stype) {
        double ret = 0;
        for (Ledger l : data) {
            if (l.getStype().getName().equals(stype)) {
                ret += l.getAmount();
            }
        }
        return Utils.convertDouble(ret);
    }

    private double printCredit(List<Ledger> data, String label, int lid, double budget) {
        double totalm = 0;
        double totalnm = 0;

        for (Ledger l : data) {
            if ((l.getLabel().getId() == lid) && (l.getLtype().getId() == 3)) {
                totalm += l.getAmount();
            }
        }
        for (Ledger l : data) {
            if ((l.getLabel().getId() == lid) && (l.getLtype().getId() != 3)) {
                totalnm += l.getAmount();
            }
        }

        double total = (totalm == 0) ? totalnm : totalm;
        total = Utils.convertDouble(total);
        double net = Utils.convertDouble((total * (-1)) - budget);
        MRBean b = new MRBean(label,total, budget, net);
        bdata.add(b);

        return totalnm;
    }

    private double allCredit(List<Ledger> data)  {
        double camt = 0;

        camt += creditAnywhere(data,  11209);
        camt += creditAnywhere(data,   10178);
        camt += creditAnywhere(data,   12933);
        camt += creditAnywhere(data, 10264);
        camt += creditAnywhere(data,  10019);

        return camt;
    }

    private double creditAnywhere(List<Ledger> data,int lid) {
        double total = 0;

        for (Ledger l : data) {
            if (l.getLabel().getId() == lid) {
                total += l.getAmount();
            }
        }
        total = Utils.convertDouble(total);

        return total;
    }

    private void printSpent(String lt, SessionDTO session, double budget)  {
        Ltype ltype = repos.getLtypeRepository().findByName(lt);
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data = ld.filterByDate(session, null, ltype);

        double total = 0;
        for (Ledger l : data) {
            if (l.getAmount() < 0)
                total += l.getAmount();
        }
        total = Utils.convertDouble(total);
        double net = Utils.convertDouble((total * (-1)) - budget);
        MRBean b = new MRBean(lt, total, budget, net);

        cdata.add(b);

    }

    private boolean isDog(Ledger l) {
        boolean ret = false;

        if (l.getLabel().getId() == 13137) {
            return true;
        }

        if (l.getChecks() != null) {
            Checks c = l.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                ret = true;
            }
        }
        return ret;
    }

    private double dogMisc(List<Ledger> data) {
        double total = 0;

        for (Ledger l : data) {
            if (l.getChecks() != null) {
                Checks c = l.getChecks();
                if (c.getPayee().getName().equals("dog")) {
                    total += l.getAmount();
                }
            }
        }
        return Utils.convertDouble(total);
    }
    private double dog(List<Ledger> data, double budget)  {

        double total = 0;

        for (Ledger l : data) {
            if (isDog(l)) {
                    total += l.getAmount();
            }
        }
        total = Utils.convertDouble(total);
        double net = Utils.convertDouble((total * (-1)) - budget);
        MRBean b = new MRBean("Dog",total, budget, net);
        bdata.add(b);
        return Utils.convertDouble(total);
    }

    private void printStype(String stype,  List<Ledger> bundle, double budget)  {
        double dvalue = 0;
        for (Ledger l : bundle) {
            Stype s = l.getStype();
            if (s.getName().equals(stype)) {
                dvalue += l.getAmount();
            }
        }

        double value = Utils.convertDouble(dvalue);
        double net = Utils.convertDouble((value * (-1)) - budget);
        MRBean b = new MRBean(stype, value, budget, net);
        bdata.add(b);
    }

}
