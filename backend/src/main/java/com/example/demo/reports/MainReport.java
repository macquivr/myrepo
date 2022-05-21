package com.example.demo.reports;

import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainReport implements ReportI {
    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public MainReport(Repos r) {
        repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
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
        double camt = 0;
        double utils = printUtils(w,data,500);
        printStype("POS",w,data, 1000);
        printStype("ATM",w,data, 300);
        double dogAmt = dog(w,data,240);
        camt += printCredit(w, data,"Usaa",  11209, 1000);
        camt += printCredit(w, data,"Aaa",   10178,400);
        camt += printCredit(w, data,"CapitalOne", 10264,  1000);
        camt += printCredit(w, data,"Amazon",   10019, 400);
        bdata.Print(w);
        w.write("\n");

        double otherBills = Utils.convertDouble(getStype(data,"Bills") + (utils * (-1)));

        w.write("OtherBills: " + otherBills + "\n");

        double miscNoDog = Utils.convertDouble(getStype(data,"Misc") + (dogAmt * (-1)));

        w.write("Misc: " + miscNoDog + "\n");

        w.write("Annual: " + getStype(data, "Annual") + "\n");

        camt = Utils.convertDouble(camt);

        double otherCredit = Utils.convertDouble(getStype(data, "Credit") + (camt * (-1)));
        w.write("Other Credit: " + otherCredit + "\n");

        double totalOut = getTotalOut(data);
        w.write("TotalOut: " + totalOut + "\n");

        double totalIn = getStype(data, "Deposit");
        w.write("TotalIn: " + totalIn + "\n");

        w.write("Net: " + Utils.convertDouble(totalIn + totalOut));

        w.write("\n");
        w.write("\n");

        printMisc(w,data);
        w.write("\n");
        w.write("\n");

        w.write("Credit spent calendar based.\n");
        printSpent(w, "Usaa",  session, 1000);
        printSpent(w, "Aaa",  session, 400);
        printSpent(w, "CapitalOne",  session, 1000);
        printSpent(w, "Amazon",  session, 400);
        w.write("\n");
        w.write("\n");
        cdata.Print(w);

    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

    private void printMisc(FileWriter w, List<Ledger> data) throws Exception
    {
        w.write("Misc Entries...\n");
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Misc") && !isDog(l)) {
                if (l.getChecks() != null) {
                    w.write(l.getChecks().getPayee().getName() + " " + l.getAmount() + "\n");
                } else {
                    w.write(l.getLabel().getName() + " " + l.getAmount() + "\n");
                }
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
    private void printStypes(FileWriter w, List<Ledger> bundle) throws Exception {
        HashMap<Stype,Double> map = new HashMap<Stype,Double>();
        for (Ledger l : bundle) {
            Stype s = l.getStype();
            Double d = map.get(s);
            if (d == null)
                map.put(s,l.getAmount());
            else {
                double dv = d.doubleValue() + l.getAmount().doubleValue();
                Double ndv = new Double(dv);
                map.put(s,ndv);
            }
        }
        Set<Stype> keys = map.keySet();
        for (Stype key : keys) {
            if (!key.getName().equals("Transfer")) {
                double value = Utils.convertDouble(map.get(key));
                w.write(key.getName() + "\t" + value + "\n");
            }
        }
    }

    private double printCredit(FileWriter w, List<Ledger> data, String label, int lid, double budget) throws Exception {
        double total = 0;

        for (Ledger l : data) {
            if (l.getLabel().getId() == lid) {
                total += l.getAmount();
            }
        }
        total = Utils.convertDouble(total);
        double net = Utils.convertDouble((total * (-1)) - budget);
        MRBean b = new MRBean(label,total, budget, net);
        bdata.add(b);

        return total;
    }

    private void printSpent(FileWriter w, String lt, SessionDTO session, double budget) throws Exception {
        int label = 0;
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
        if (l.getChecks() != null) {
            Checks c = l.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                return true;
            }
        }
        return false;
    }

    private double dog(FileWriter w, List<Ledger> data, double budget) throws Exception {
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

    private double printUtils(FileWriter w, List<Ledger> data, double budget) throws Exception
    {
        double total = 0;

        for (Ledger l : data) {
            if (l.getLabel().getId() == 10344) {
                total += l.getAmount();
            }
        }

        double net = Utils.convertDouble((total * (-1)) - budget);
        MRBean mb = new MRBean("Utils", total, budget, net);
        bdata.add(mb);

        return total;
    }

    private double printStype(String stype, FileWriter w, List<Ledger> bundle, double budget) throws Exception {
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

        return value;
    }

}
