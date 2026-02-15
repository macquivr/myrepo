package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.bean.tables.InOutTable;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class SimpleReport implements ReportI {
    private final Repos repos;

    public SimpleReport(Repos r) {

        this.repos = r;
    }

    private void adjustAnnual(List<Ledger> adata, List<Ledger> data) {
        for (Ledger l : data) {
            Checks c = l.getChecks();
            if ((c != null) && (c.getPayee().getCheckType().getName().equals("Annual"))) {
                if (!adata.contains(l)) {
                    adata.add(l);
                }
            }

        }

    }

    private void dumpTransfer(List<Ledger> data) {
        List<Ledger> death = new ArrayList<Ledger>();

        for (Ledger l : data) {
            if (l.getStype().getId() == 8) {
                death.add(l);
            } else {
                Checks c = l.getChecks();
                if ((c != null) && (c.getPayee().getCheckType().getName().equals("Transfer"))) {
                    death.add(l);
                }
            }
        }
        data.removeAll(death);
    }

    public String go(FileWriter w, SessionDTO session) throws Exception
    {
        CategoryRepository cr = repos.getCategoryRepository();
        Category transferc = cr.findByName("Transfer");
        LData ld = new LData(repos.getLedgerRepository());

        StypeRepository srepo = repos.getStypeRepository();
        Stype annual = srepo.findByName("Annual");
        List<Ledger> adata = ld.filterByDate(session,annual,null);

        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        dumpTransfer(data);
        adjustAnnual(adata,data);

        StartStop dates = ld.getDates();

        DataUtils du = new DataUtils(repos);
        HashMap<Lenum, Data> dmap = du.populateDmap(session,dates);

        double total = 0;
        printPeriod(w,dates);

        double inTotal = doIn(w,data);


        double pos = printStype(null,"POS",  w, data,false);
        line(w,"POS", pos);
        total += pos;

        double atm = printStype(null,"ATM",  w, data,false);
        line(w,"ATM", atm);
        total += atm;

        w.write("\nMortgageAndLifeInsurance:\n");
        List<Integer> lbls = new ArrayList<Integer>();
        lbls.add(12712);
        lbls.add(11451);
        double mi = printLabel(lbls,  w, data, false);
        line(w,"  Total:", mi);
        total += mi;


        double lm = printLastMonth(w,data);
        line(w,"  Total", lm);
        total += lm;

        double annuala = printStype("Annual", "Annual",w,data,true);
        line(w,"  Total", annuala);

        total += annuala;
        w.write("\n");

        w.write("Everything Else:\n");
        double ee = printLabel(null,  w, data, true);
        line(w,"  Total", ee);

        total = Utils.convertDouble(total + ee);

        w.write("\nTotal: " + total);
        w.write("\nIn Total: " + inTotal);
        double net = Utils.convertDouble(inTotal + total);
        w.write("\nNet: " + net);
        return null;
    }

    private double doIn(FileWriter w, List<Ledger> data) throws Exception {
        double ret = 0;
        double inv = 0;
        double other = 0;
        List<Ledger> death = new ArrayList<Ledger>();

        for (Ledger l : data) {
            if (l.getAmount() > 0) {
                ret += l.getAmount();
                death.add(l);
                if (l.getLabel().getId() == 12448) {
                    inv += l.getAmount();
                } else {
                    other += l.getAmount();
                }
            }
        }
        w.write("In\n");
        line(w,"  Work: ", Utils.convertDouble(inv));
        line(w,"  Other: ",Utils.convertDouble(other));
        data.removeAll(death);
        w.write("\n");
        return Utils.convertDouble(ret);
    }

    private double printLastMonth(FileWriter w, List<Ledger> data) throws Exception {
        double ret = printStype("LastMonth", "Credit",w,data,true);
        List<Integer> lbls = new ArrayList<Integer>();
        lbls.add(13137);
        lbls.add(10344);
        ret += printLabel(lbls,  w, data, false);

        return ret;
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

    private void printGlobalStat(FileWriter w, List<Ledger> data, Category transferc) throws Exception
    {
        Ion ion = InOutNet(data,transferc);
        line(w," IN: " + ion.getIn() + " OUT: " + ion.getOut() + " NET: " + ion.getNet(), null);
        w.write("\n");
    }


    private void setFb(HashMap<Lenum, Data> data, Ion obj, Lenum e)
    {
        double fb = 0;
        Data d = data.get(e);
        if (d != null) {
            obj.setLabel(d.getLabel());
            Statement st = d.getStmt();
            if (st != null) {
                fb = st.getFbalance();
            }
        }
        obj.setBalance(fb);
    }

    private void printStat(FileWriter w, HashMap<Lenum, Data> data, Category transferc) throws Exception
    {
        IonL idata = new IonL();

        Ion ionm = InOutNet(data.get(Lenum.MAIN).getLdata(),transferc);
        idata.add(ionm);
        setFb(data, ionm,Lenum.MAIN);

        Ion ionms = InOutNet(data.get(Lenum.MAINSAVE).getLdata(),transferc);
        idata.add(ionms);
        setFb(data, ionms,Lenum.MAINSAVE);

        Ion ionmortg = InOutNet(data.get(Lenum.MORTG).getLdata(),transferc);
        idata.add(ionmortg);
        setFb(data, ionmortg,Lenum.MORTG);

        Ion ionslush = InOutNet(data.get(Lenum.SLUSH).getLdata(),transferc);
        idata.add(ionslush);
        setFb(data, ionslush,Lenum.SLUSH);

        Ion ionannual = InOutNet(data.get(Lenum.ANNUAL).getLdata(),transferc);
        idata.add(ionannual);
        setFb(data, ionannual,Lenum.ANNUAL);

        Ion ionml = InOutNet(data.get(Lenum.ML).getLdata(),transferc);
        idata.add(ionml);
        setFb(data, ionml,Lenum.ML);

        InOutTable t = new InOutTable();
        t.populateTable(idata.getData());
        t.Print(w);

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
        double total = 0;
        Set<Stype> keys = map.keySet();
        for (Stype key : keys) {
            if (!key.getName().equals("Transfer")) {
                double value = Utils.convertDouble(map.get(key));
                line(w,key.getName() + "\t",value);
                total += value;
            }
        }
        line(w,"TOTAL",Utils.convertDouble(total));
    }

    private void printSpent(FileWriter w, SessionDTO session) throws Exception
    {
        w.write("\n");
        w.write("Credit spent\n");

        printSpent(w, "Usaa",  session);
        printSpent(w, "Aaa",  session);
        printSpent(w, "CapitalOne",  session);
        printSpent(w, "Amazon",  session);
    }

    private void printSpent(FileWriter w, String lt, SessionDTO session) throws Exception
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

    private void printCategories(FileWriter w,SessionDTO session) throws Exception
    {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterLow(data);

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

    private double printLabel(List<Integer> lbls, FileWriter w, List<Ledger> bundle, boolean all) throws Exception
    {
        double ret = 0;

        HashMap<String, Double> map = new HashMap<>();
        List<Ledger> death = new ArrayList<Ledger>();

        for (Ledger l : bundle) {
            Label lb = l.getLabel();
            if (all || (!all && lbls.contains(lb.getId()))) {
                death.add(l);
                String lstr = lb.getNames().getName();

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
            ret += d;
            w.write("  " + key + " " + d + "\n");
        }
        bundle.removeAll(death);
        return Utils.convertDouble(ret);
    }

    private double printStype(String label, String stype, FileWriter w, List<Ledger> bundle, boolean pr) throws Exception
    {
        double ret = 0;
        if (pr) {
            w.write("\n");
            w.write(label + "\n");
        }
        HashMap<String, Double> map = new HashMap<>();
        boolean ok = false;
        List<Ledger> death = new ArrayList<Ledger>();

        for (Ledger l : bundle) {
            ok = false;
            Stype s = l.getStype();
            if (s.getName().equals(stype)) {
                ok = true;
            } else {
                if (l.getChecks() != null) {
                    Checks c = l.getChecks();
                    Checktype ct = c.getPayee().getCheckType();
                    if (ct.getName().equals(stype)) {
                        ok = true;
                    }
                }
            }
            if (ok) {
                death.add(l);
                String lstr;
                if (l.getChecks() != null) {
                    lstr = l.getChecks().getPayee().getName();
                    Checks c = l.getChecks();
                    Checktype ct = c.getPayee().getCheckType();
                    if (!ct.getName().equals(stype)) {
                        continue;
                    }
                }
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
            ret += d;
            if (pr) {
                w.write("  " + key + " " + d + "\n");
            }
        }
        bundle.removeAll(death);
        return Utils.convertDouble(ret);
    }

    private Ion InOutNet(List<Ledger> data, Category transferc) {
        Ion ret = new Ion();

        for (Ledger l : data) {
            Label lbl = l.getLabel();
            Category c = lbl.getCategory();
            if ((c.getId() != transferc.getId()) && (l.getStype().getId() != 8)){
                if (l.getAmount() > 0) {
                    ret.setIn(ret.getIn() + l.getAmount());
                }
                if (l.getAmount() < 0) {
                    ret.setOut(ret.getOut() + l.getAmount());
                }
            }
        }
        ret.setNet(ret.getIn() + ret.getOut());

        return ret;
    }
}

