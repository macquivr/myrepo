package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StypeRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InOutReport implements ReportI {
    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public InOutReport(Repos r) {
        repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public double pc()
    {
        return 0.0;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception
    {
        StypeRepository sr = repos.getStypeRepository();
        Stype transfert = sr.findByName("Transfer");

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);

        DataUtils du = new DataUtils(repos);
        HashMap<Lenum, Data> dmap = du.populateDmap(session,dates);

        printInOut(w,dmap, transfert);
    }


    private Ion InOutNet(List<Ledger> data, Stype transfert) {
        Ion ret = new Ion();

        for (Ledger l : data) {
            Stype s = l.getStype();
            if (s.getId() != transfert.getId()) {
                if (l.getAmount() > 0)
                    ret.setIn(ret.getIn() + l.getAmount());
                if (l.getAmount() < 0)
                    ret.setOut(ret.getOut() + l.getAmount());
            } else {
                if (l.getAmount() > 0)
                    ret.setTIn(ret.getTIn() + l.getAmount());
                if (l.getAmount() < 0)
                    ret.setTOut(ret.getTOut() + l.getAmount());
            }
        }
        ret.setNet(ret.getIn() + ret.getOut());

        return ret;
    }

    private IonL makeList(HashMap<Lenum, Data> data, Stype transfert) {
        IonL l = new IonL();

        Data d = data.get(Lenum.MAINSAVE);

        Ion ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);

        l.add(ion);

        d = data.get(Lenum.MAIN);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.MORTG);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.SLUSH);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.ANNUAL);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.ML);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        return l;
    }
    private void printInOut(FileWriter w, HashMap<Lenum, Data> data, Stype transfert) throws Exception
    {
        IonL l = makeList(data,transfert);

        w.write("               " + l.getColumInLabel() + l.getColumOutLabel() + l.getColumnTInLabel() + l.getColumnTOutLabel() + l.getColumnTnetLabel() + " Fbal\n");
        List<Ion> dl = l.getData();
        double tf = 0;
        for (Ion ion : dl) {
            Data d = ion.getData();
            w.write(d.getLabel() + l.getInLabel(ion) + " " + l.getOutLabel(ion) + " " + l.getTInLabel(ion) + " " + l.getTOutLabel(ion) + " " + l.getTnetLabel(ion));
            if (d.getStmt() != null) {
                w.write(" " + d.getStmt().getFbalance());
                tf += d.getStmt().getFbalance();
            }
            w.write("\n");
        }
        tf = Utils.convertDouble(tf);
        Ion ion = l.getTotal();
        w.write("Total         " + l.getInLabel(ion) + " " + l.getOutLabel(ion) + " " + l.getTInLabel(ion) + " " + l.getTOutLabel(ion) + " " + l.getTnetLabel(ion) + " " + tf);
        w.write("\n");
    }

    private void line(FileWriter w, String label, Double value) throws Exception {
        if (value != null)
            w.write(label + " " + value + "\n");
        else
            w.write(label + "\n");
    }
    private double Pb(FileWriter w, Lenum e, HashMap<Lenum, Data> data, double total) throws Exception{
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

        total = Pb(w,Lenum.MAINSAVE,data,total);
        total = Pb(w,Lenum.MAIN,data,total);
        total = Pb(w,Lenum.MORTG,data,total);
        total = Pb(w,Lenum.SLUSH,data,total);
        total = Pb(w,Lenum.ANNUAL,data,total);
        total = Pb(w,Lenum.ML,data,total);

        total = Utils.convertDouble(total);
        w.write("Total: " + total);
        w.write("\n\n");
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

    private double allCredit(List<Ledger> data) throws Exception {
        double camt = 0;

        camt += creditAnywhere(data,  11209);
        camt += creditAnywhere(data,   10178);
        camt += creditAnywhere(data,   12933);
        camt += creditAnywhere(data, 10264);
        camt += creditAnywhere(data,  10019);

        return camt;
    }

    private double creditAnywhere(List<Ledger> data,int lid) throws Exception {
        double total = 0;

        for (Ledger l : data) {
            if (l.getLabel().getId() == lid) {
                total += l.getAmount();
            }
        }
        total = Utils.convertDouble(total);

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
