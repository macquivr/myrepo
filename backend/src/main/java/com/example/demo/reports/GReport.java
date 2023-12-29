package com.example.demo.reports;

import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.Ledger;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class GReport implements ReportI {
    private Repos repos = null;

    public GReport(Repos r) {
        this.repos = r;
    }
    public void go(FileWriter w, SessionDTO session) throws Exception {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        RMap r = new RMap(data);
        printPeriod(w,dates);

        List<Catsort> summary = new Vector<Catsort>();

        List<Catsort> ina = r.apply(new RMapIn());
        List<Catsort> atm = getData(r,"Atm", new RMapAtm(), summary);
        List<Catsort> pos = getData(r,"POS", new RMapPos(), summary);
        List<Catsort> util = getData(r,"Util", new RMapUtils(), summary);
        List<Catsort> emma = getData(r,"Emma", new RMapEmma(), summary);
        List<Catsort> dog = getData(r,"Dog", new RMapDog(), summary);
        List<Catsort> credit = getData(r,"Credit", new RMapCredit(), summary);
        List<Catsort> bills = getData(r,"Bills", new RMapBills(), summary);
        List<Catsort> sears = getData(r,"Sears", new RMapSears(), summary);

        List<Catsort> annual = Annual(r,summary);
        double t = doTotal(annual);
        doSummary("Annual", t, summary);

        List<Catsort> misc = r.apply(new RMapRest(false));
        t = doTotal(misc);
        doSummary("Misc",t,summary);

        List<Catsort> miscChecks = r.apply(new RMapRest(true),true);
        t = doTotal(miscChecks);
        doSummary("MiscC",t,summary);

        Collections.sort(summary);

        //p(w,"Summary",summary,false);

        double tin = doTotal(ina);
        double out = doTotal(summary);
        double net = Utils.convertDouble(tin + out);

        percent(w,"Percent", summary, out);

        Collections.sort(ina, Collections.reverseOrder());
        p(w,"In",ina,false);
        w.write("NET: " + net + "\n\n");

        p(w,"Atm",atm,true);
        p(w,"POS",pos,true);
        p(w,"Utils",util,true);
        p(w,"Emma",emma,true);
        p(w,"Dog",dog,true);
        p(w,"Credit",credit,false);
        p(w,"Bills", bills,false);
        p(w,"Sears",sears,true);
        p(w,"Annual",annual,false);
        pMisc(w, misc,miscChecks);
        //p(w,"Misc Checks", miscChecks,false);
    }

    private List<Catsort> getData(RMap r, String label, RMapI ri, List<Catsort> s) {
        List<Catsort> ret  = r.apply(ri);
        double total = doTotal(ret);
        doSummary(label,total,s);

        return ret;
    }

    private void Rest(FileWriter w, RMap r,List<Catsort> s) throws Exception
    {
        double total = 0;

        List<Catsort> data = r.apply(new RMapRest(false));

        total = doTotal(data);
        //total = p(w,"Misc", data);

        data = r.apply(new RMapRest(true),true);

        //total += p(w, "Misc Checks", data);
        total += doTotal(data);

        doSummary("Misc",total,s);
    }

    private void doSummary(String label, double amount, List<Catsort> s) {
        Catsort sobj = new Catsort();
        sobj.setLabel(label);
        sobj.setAmount(Utils.convertDouble(amount));
        s.add(sobj);
    }

    private void generic(FileWriter w, RMap r, List<Catsort> s,String label, RMapI ri) throws Exception
    {
        List<Catsort> data = r.apply(ri);

        double t = p(w,label, data,false);
        doSummary(label, t, s);
    }

    private void genericT(FileWriter w, RMap r, List<Catsort> s, String label, RMapI ri) throws Exception
    {
        List<Catsort> data = r.apply(ri);
        double total = 0;
        for (Catsort c : data) {
            total += c.getAmount();
        }
        total = Utils.convertDouble(total);
        w.write(label + ": " + total + "\n\n");
        doSummary(label,total,s);
    }

    private void In(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        generic(w,r,s, "In", new RMapIn());
    }

    private void Atm(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        generic(w,r,s, "Atm", new RMapAtm());
    }

    private List<Catsort> Annual(RMap r, List<Catsort> s) throws Exception
    {
        List<Catsort> data = r.apply(new RMapAnnual(false));
        List<Catsort> annualChecks = r.apply(new RMapAnnual(true),true);
        List<Catsort> adata = new Vector<Catsort>();
        adata.addAll(data);
        adata.addAll(annualChecks);
        Collections.sort(adata);

        return adata;

    }

    private void Bills(FileWriter w, RMap r,List<Catsort> s) throws Exception
    {
        generic(w,r,s,"Bills", new RMapBills());
    }

    private void Credit(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"Credit", new RMapCredit());
    }

    private void Sears(FileWriter w, RMap r,List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"Sears", new RMapSears());
    }

    private void Emma(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"Emma",new RMapEmma());
    }

    private void Dog(FileWriter w, RMap r,List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"Dog", new RMapDog());
    }
    private void Pos(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"POS",new RMapPos());
    }

    private void Util(FileWriter w, RMap r, List<Catsort> s) throws Exception
    {
        genericT(w,r,s,"Utils",new RMapUtils());
    }

    private void pMisc(FileWriter w, List<Catsort> misc, List<Catsort> miscChecks) throws Exception {
        w.write("Misc:\n");
        double total = 0;
        double mt = 0;
        double mtc = 0;

        for (Catsort c : misc) {
            w.write("  " + c.getLabel() + " " + c.getAmount() + "\n");
        }
        w.write("\n");
        w.write("  Checks:\n");
        for (Catsort c : miscChecks) {
            w.write("    " + c.getLabel() + " " + c.getAmount() + "\n");
        }
    }

    private double p(FileWriter w, String label, List<Catsort> data, boolean s) throws Exception
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
        return total;
    }

    private void percent(FileWriter w, String label, List<Catsort> data, double totalo) throws Exception
    {
        w.write(label + "\n");
        double tp = 0;

        for (Catsort c : data) {
            double a = c.getAmount();
            double p = a/totalo;
            double percent = Utils.convertDouble(p * 100);
            w.write(c.getLabel() + ":\t" + a + "\t" + percent + "\n");
            tp += percent;
        }
        tp = Utils.convertDouble(tp);
        w.write("TP: " + tp + "\n\n");
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
