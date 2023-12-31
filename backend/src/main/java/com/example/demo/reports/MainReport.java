package com.example.demo.reports;

import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainReport implements ReportI {
    private final Repos repos;
    private MRBeanl bdata;
    private MRBeanl cdata;

    public MainReport(Repos r) {
        this.repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    private List<Ledger> getBillsNoUtils(List<Ledger> data) {
        List<Ledger> r = new ArrayList<>();

        for (Ledger l : data) {
            if ((l.getStype().getName().equals("Bills")) &&
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
    public void go(FileWriter w, SessionDTO session) throws Exception
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

        printStype("POS",data, posBudget);
        printStype("ATM",data, atmBudget);
        double dogAmt = dog(data,dogBudget);
        slushCredit += printCredit(data,"Usaa",  11209, usaaBudget);
        slushCredit += printCredit(data,"Aaa",   12933,aaaBudget);
        slushCredit += printCredit(data,"CapitalOne", 10264,  capOneBudget);
        slushCredit += printCredit(data,"Amazon",   10019, amazonBudget);
        bdata.Print(w);
        w.write("\n");

        w.write("Credit Overage: " + Utils.convertDouble(slushCredit) + "\n");

        double otherBills = 0;
        List<Ledger> obills = getBillsNoUtils(data);
        for (Ledger l : obills) {
            otherBills = Utils.convertDouble(otherBills + l.getAmount());
        }
        w.write("OtherBills: " + otherBills + "\n");

        double miscNoDog = Utils.convertDouble(getMiscOut(data) + (dogAmt * (-1)));
        w.write("Misc: " + miscNoDog + "\n");

        double annual = getStype(data, "Annual");

        w.write("Annual: " + annual + "\n");

        camt = allCredit(data);
        camt = Utils.convertDouble(camt);

        double otherCredit = Utils.convertDouble(getStype(data, "Credit") + (camt * (-1)));
        w.write("Other Credit: " + otherCredit + "\n");

        double totalOut = getTotalOut(data);
        w.write("TotalOut: " + totalOut + "\n");

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
        if (l.getChecks() != null) {
            Checks c = l.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                ret = true;
            }
        }
        return ret;
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
