package com.example.demo.reports;

import com.example.demo.bean.AllP;
import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.Category;
import com.example.demo.domain.Ledger;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class CatReport implements ReportI {

    private LedgerRepository lrepo = null;
    private AllP allData;

    private int ryear;

    public CatReport(Repos r) {
        this.lrepo = r.getLedgerRepository();

    }

    public CatReport(LedgerRepository lrepo) {
        this.lrepo = lrepo;
    }
    public AllP getAllData() {
        return this.allData;
    }
    public String go(FileWriter w, SessionDTO session) throws Exception {

        LData ld = new LData(lrepo);
        List<Ledger> data  = ld.filterByDate(session,null,null);
        StartStop dates = ld.getDates();

        if (w != null) {
            printPeriod(w, dates);
        }
        List<Catsort> cat = new Vector<>();
        List<Ledger> death = new ArrayList<Ledger>();
        for (Ledger d : data) {
            if ((d.getLtype().getId() > 14) || (d.getLtype().getId() == 13)) {
                death.add(d);
            }
        }
        data.removeAll(death);
        HashMap<Category, Catsort> map = new HashMap<Category, Catsort>();
        for (Ledger d : data) {
            Category c = d.getLabel().getCategory();
            Catsort cs = map.get(c);
            if (cs == null) {
                cs = new Catsort();
                cs.setLabel(c.getName());
                cs.setAmount(d.getAmount());
                cat.add(cs);
                map.put(c,cs);
            } else {
                cs.setAmount(Utils.convertDouble(cs.getAmount() + d.getAmount()));
            }
        }
        Collections.sort(cat);
        if (w != null) {
            for (Catsort c : cat) {
                w.write(c.getLabel() + " " + c.getAmount() + "\n");
            }
        }
        return null;
    }

    private List<Catsort> getData(RMap r, String label, RMapI ri, List<Catsort> s) {
        List<Catsort> ret  = r.apply(ri);
        double total = doTotal(ret);
        doSummary(label,total,s);

        return ret;
    }

    private void doSummary(String label, double amount, List<Catsort> s) {
        Catsort sobj = new Catsort();
        sobj.setLabel(label);
        sobj.setAmount(Utils.convertDouble(amount));
        s.add(sobj);
    }

    private List<Catsort> Annual(RMap r)
    {
        List<Catsort> data = r.apply(new RMapAnnual(false));
        List<Catsort> annualChecks = r.apply(new RMapAnnual(true),true);
        List<Catsort> adata = new Vector<>();
        adata.addAll(data);
        adata.addAll(annualChecks);
        Collections.sort(adata);

        return adata;

    }

    private void pMisc(FileWriter w, List<Catsort> misc, List<Catsort> miscChecks) throws Exception {
        w.write("Misc:\n");

        for (Catsort c : misc) {
            w.write("  " + c.getLabel() + " " + c.getAmount() + "\n");
        }
        w.write("\n");
        w.write("  Checks:\n");
        for (Catsort c : miscChecks) {
            w.write("    " + c.getLabel() + " " + c.getAmount() + "\n");
        }
    }

    private void p(FileWriter w, String label, List<Catsort> data, boolean s) throws Exception
    {
        if (!s) {
            w.write(label + "\n");
        }
        double total = 0;
        for (Catsort c : data) {
            if (!s) {
                w.write("  " + c.getLabel() + ": " + c.getAmount() + "\n");
            }
            total += c.getAmount();
        }
        total = Utils.convertDouble(total);
        if (!s) {
            w.write("Total: " + total + "\n\n");
        } else {
            w.write(label + ": " + total + "\n\n");
        }
    }

    private void setAllP(Catsort c, double a, double p, double t) {
        String key = c.getLabel();

        System.out.println("RYEAR: " + this.ryear + " " + c.getLabel() + " " + p + " " + a + " " + t);
        if (key.equals("Atm")) {
            this.allData.setAtm(p);
        }
        if (key.equals("POS")) {
            this.allData.setPos(p);
        }
        if (key.equals("Util")) {
            this.allData.setUtils(p);
        }
        if (key.equals("Credit")) {
            this.allData.setCredit(p);
        }
        if (key.equals("Bills")) {
            this.allData.setBills(p);
        }
        if (key.equals("Sears")) {
            this.allData.setSears(p);
        }
        if (key.equals("Misc")) {
            this.allData.setMisc(p);
        }
        if (key.equals("MiscC")) {
            this.allData.setMiscC(p);
        }
        if (key.equals("Annual")) {
            this.allData.setAnnual(p);
        }
    }
    private void percent(FileWriter w, List<Catsort> data, double totalo) throws Exception
    {
        if (w != null) {
            w.write("Amount and Percent\n");
        }
        double tp = 0;

        for (Catsort c : data) {
            double a = c.getAmount();
            double p = a/totalo;
            double percent = Utils.convertDouble(p * 100);
            if (w != null) {
                w.write(c.getLabel() + ":\t" + a + "\t" + percent + " " + totalo + "\n");
            }
            setAllP(c,a,percent,totalo);

            tp += percent;
        }
        tp = Utils.convertDouble(tp);
        if (w != null) {
            w.write("TP: " + tp + "\n\n");
        }
    }

    private double doTotal(List<Catsort> data)
    {
        double total = 0;
        for (Catsort c : data) {
            total += c.getAmount();
        }
        return  Utils.convertDouble(total);
    }

    private void printPeriod(FileWriter w, StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }
}
