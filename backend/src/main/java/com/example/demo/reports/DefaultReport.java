package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.bean.tables.InOutTable;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.*;
import com.example.demo.importer.Repos;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class DefaultReport implements ReportI {
    private final Repos repos;

    public DefaultReport(Repos r) {

        this.repos = r;
    }

    private void adjustAnnual(List<Ledger> adata, List<Ledger> data) {
        for (Ledger l : data) {
            Checks c = l.getChecks();
            if ((c != null) && (c.getPayee().getCheckType().getName().equals("Annual"))) {
                adata.add(l);
            }

        }

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
        adjustAnnual(adata,data);

        StartStop dates = ld.getDates();

        DataUtils du = new DataUtils(repos);
        HashMap<Lenum, Data> dmap = du.populateDmap(session,dates);

        printPeriod(w,dates);
        printGlobalStat(w,data,transferc);
        printBalances(w,dmap);
        printStat(w, dmap, transferc);
        printStypes(w, data);

        double t = printStype("Annual",w,adata);
        w.write("Total: " + t + "\n\n");

        printStype("Bills",w,data);
        printUtils(w,dates);
        printStype("Credit",w,data);
        //printStype("Annual",w,adata);
        printStype("Misc",w,data);
        printSpent(w, session);
        printCategories(w,session);

        return null;
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

    private double printStype(String stype, FileWriter w, List<Ledger> bundle) throws Exception
    {
        double ret = 0;
        w.write("\n");
        w.write("Stype " + stype + "\n");
        HashMap<String, Double> map = new HashMap<>();
        boolean ok = false;

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
            w.write(key + " " + d + "\n");
        }
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

