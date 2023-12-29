package com.example.demo.utils.rutils;

import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.Ledger;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class OutUtils {
    private Repos repos;

    private List<Catsort> summary = null;
    private double totalo = 0;

    public OutUtils(Repos r) {
        this.repos = r;
        this.summary = new Vector<Catsort>();
    }

    public void go(List<Ledger> ldata) {

        RMap r = new RMap(ldata);

        List<Catsort> ina = r.apply(new RMapIn());
        List<Catsort> atm = getlData(r,"Atm", new RMapAtm(), summary);
        List<Catsort> pos = getlData(r,"POS", new RMapPos(), summary);
        List<Catsort> util = getlData(r,"Util", new RMapUtils(), summary);
        List<Catsort> emma = getlData(r,"Emma", new RMapEmma(), summary);
        List<Catsort> dog = getlData(r,"Dog", new RMapDog(), summary);
        List<Catsort> credit = getlData(r,"Credit", new RMapCredit(), summary);
        List<Catsort> bills = getlData(r,"Bills", new RMapBills(), summary);
        List<Catsort> sears = getlData(r,"Sears", new RMapSears(), summary);

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

        this.totalo = doTotal(summary);
    }
    private void doSummary(String label, double amount, List<Catsort> s) {
        Catsort sobj = new Catsort();
        sobj.setLabel(label);
        sobj.setAmount(Utils.convertDouble(amount));
        s.add(sobj);
    }
    private double doTotal(List<Catsort> data)
    {
        double total = 0;
        for (Catsort c : data) {
            total += c.getAmount();
        }
        return  Utils.convertDouble(total);
    }
    private List<Catsort> Annual(RMap r, List<Catsort> s)
    {
        List<Catsort> data = r.apply(new RMapAnnual(false));
        List<Catsort> annualChecks = r.apply(new RMapAnnual(true),true);
        List<Catsort> adata = new Vector<Catsort>();
        adata.addAll(data);
        adata.addAll(annualChecks);
        Collections.sort(adata);

        return adata;

    }
    private List<Catsort> getlData(RMap r, String label, RMapI ri, List<Catsort> s) {
        List<Catsort> ret  = r.apply(ri);
        double total = doTotal(ret);
        doSummary(label,total,s);

        return ret;
    }

    public List<Catsort> getData() { return this.summary; }

    public double getTotalo() { return this.totalo; }

}
