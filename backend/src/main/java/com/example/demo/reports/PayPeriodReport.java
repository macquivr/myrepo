package com.example.demo.reports;

import com.example.demo.bean.AllP;
import com.example.demo.bean.Catsort;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.utils.*;
import com.example.demo.repository.KvpRepository;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.repository.WdatamapRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class PayPeriodReport implements ReportI {

    private Repos repos = null;
    private LedgerRepository lrepo = null;

    private AllP allData;

    private int ryear;

    public PayPeriodReport(Repos r) {
        this.repos = r;
        this.lrepo = r.getLedgerRepository();
    }

    public PayPeriodReport(LedgerRepository lrepo) {
        this.lrepo = lrepo;
    }
    public AllP getAllData() {
        return this.allData;
    }
    public void go(FileWriter w, SessionDTO session) throws Exception {
        boolean firstHalf = true;
        Consolidate c = session.getConsolidate();
        if (c.equals(Consolidate.HALF)) {
            firstHalf = false;
        }

        LocalDate start = session.getStart();
        LocalDate stop = session.getStop();
        PayperiodRepository pr = repos.getPayPeriod();
        WdatamapRepository wr = repos.getPayWdatamap();

        System.out.println("START: " + start.toString() + " STOP: " + stop.toString() + "\n");
        List<Payperiod> ppl = pr.findAllByStartBetweenOrderByStartAsc(start,stop);
        if ((ppl == null) || (ppl.isEmpty())) {
            System.out.println("Couldn't find period....");
            return;
        }
        Payperiod pp = (firstHalf) ? ppl.get(0) : ppl.get(1);
        w.write("Start: " + pp.getStart().toString() + " Stop: " + pp.getStop().toString() + "\n");
        List<Wdatamap> whodata = wr.findAllByWidOrderByWho(pp);

        pNet(w,whodata);
        pFreq(w,whodata);
        pwho(w,whodata);
        pother(w,whodata);
        peachWho(w,whodata);
        peachOther(w,whodata);
    }

    private void pNet(FileWriter w, List<Wdatamap> data) throws Exception {
        double in = 0;
        double out = 0;
        double credit = 0;
        for (Wdatamap wd : data) {
            TLedger t = wd.getTid();
            double amount = t.getAmount();
            if (wd.getCredit() == 1) {
                credit += amount;
            } else {
                if (amount > 0) {
                    in += amount;
                } else {
                    out += amount;
                }
            }
        }
        w.write("In: " + in + "\n");
        w.write("Out: " + out + "\n");
        w.write("Credit: " + credit + "\n");
        w.write("\n\n");
    }

    private void pFreq(FileWriter w, List<Wdatamap> data) throws Exception {
        w.write("Freq:\n");
        HashMap<Kvp, Double> map = new HashMap<Kvp, Double>();
        for (Wdatamap obj : data) {
            Double d = map.get(obj.getFreq());
            if (d == null) {
                map.put(obj.getFreq(),obj.getTid().getAmount());
            } else {
                double nd = Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                map.put(obj.getFreq(),nd);
            }
        }
        List<Catsort> p = new ArrayList<Catsort>();
        Set<Kvp> keys = map.keySet();
        for (Kvp key : keys) {
            Catsort c = new Catsort();
            c.setLabel(key.getName());
            c.setAmount(map.get(key));
            p.add(c);
        }
        Collections.sort(p);
        for (Catsort obj : p) {
            w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
        }
        w.write("\n\n");
    }

    private void pother(FileWriter w, List<Wdatamap> data) throws Exception {
        double total = 0;
        w.write("Other Breakdown:\n");
        HashMap<Kvp, Double> map = new HashMap<Kvp, Double>();
        for (Wdatamap obj : data) {
            if ((obj.getWho().getId() != 11) || (obj.getOther() == null))
                continue;
            total += obj.getTid().getAmount();
            Double d = map.get(obj.getOther());
            if (d == null) {
                map.put(obj.getOther(),obj.getTid().getAmount());
            } else {
                double nd = Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                map.put(obj.getOther(),nd);
            }
        }
        List<Catsort> p = new ArrayList<Catsort>();
        Set<Kvp> keys = map.keySet();
        for (Kvp key : keys) {
            Catsort c = new Catsort();
            c.setLabel(key.getName());
            c.setAmount(map.get(key));
            p.add(c);
        }
        Collections.sort(p);
        for (Catsort obj : p) {
            w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
        }
        w.write("Total: " + Utils.convertDouble(total));
        w.write("\n\n");
    }

    private void peachOther(FileWriter w, List<Wdatamap> data) throws Exception {
        KvpRepository kr = repos.getKvp();
        Kvp kl = kr.findByName("otherTypes");
        List<Kvp> otherl = kr.findAllByType(kl.getId());
        double total = 0;

        for (Kvp k : otherl) {
            HashMap<Label, Double> map = new HashMap<Label, Double>();
            w.write(k.getName() + ":\n");
            total = 0;
            for (Wdatamap obj : data) {
                if (obj.getOther() == null)
                    continue;
                if (obj.getOther().getId() == k.getId()) {
                    Double d = map.get(obj.getTid().getLabel());
                    if (d == null) {
                        map.put(obj.getTid().getLabel(), obj.getTid().getAmount());
                    } else {
                        double nd = Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                        map.put(obj.getTid().getLabel(), nd);
                    }
                }
            }

            List<Catsort> p = new ArrayList<Catsort>();
            Set<Label> keys = map.keySet();
            for (Label key : keys) {
                Catsort c = new Catsort();
                c.setLabel(key.getName());
                c.setAmount(map.get(key));
                p.add(c);
            }
            Collections.sort(p);
            for (Catsort obj : p) {
                w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
                total += obj.getAmount();
            }
            total = Utils.convertDouble(total);
            w.write("Total: " + total);
            w.write("\n\n");
        }
    }
    private void peachWho(FileWriter w, List<Wdatamap> data) throws Exception {
        KvpRepository kr = repos.getKvp();
        Kvp who = kr.findByName("who");
        List<Kvp> whol = kr.findAllByType(who.getId());
        double total = 0;

        for (Kvp k : whol) {
            HashMap<Label, Double> map = new HashMap<Label, Double>();
            if (k.getId() == 11)
                continue;
            w.write(k.getName() + ":\n");
            total = 0;
            for (Wdatamap obj : data) {
                if (obj.getWho().getId() == k.getId()) {
                    Double d = map.get(obj.getTid().getLabel());
                    if (d == null) {
                        map.put(obj.getTid().getLabel(), obj.getTid().getAmount());
                    } else {
                        double nd = Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                        map.put(obj.getTid().getLabel(),nd);
                    }
                }
            }

            List<Catsort> p = new ArrayList<Catsort>();
            Set<Label> keys = map.keySet();
            for (Label key : keys) {
                Catsort c = new Catsort();
                c.setLabel(key.getName());
                c.setAmount(map.get(key));
                p.add(c);
            }
            Collections.sort(p);
            for (Catsort obj : p) {
                w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
                total += obj.getAmount();
            }
            total = Utils.convertDouble(total);
            w.write("Total: " + total);
            w.write("\n\n");
        }
    }

    private void pwho(FileWriter w, List<Wdatamap> data) throws Exception {
        w.write("Who:\n");
        HashMap<Kvp, Double> map = new HashMap<Kvp, Double>();
        for (Wdatamap obj : data) {
            Double d = map.get(obj.getWho());
            if (d == null) {
                map.put(obj.getWho(),obj.getTid().getAmount());
            } else {
                double nd = Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                map.put(obj.getWho(),nd);
            }
        }
        List<Catsort> p = new ArrayList<Catsort>();
        Set<Kvp> keys = map.keySet();
        for (Kvp key : keys) {
            Catsort c = new Catsort();
            c.setLabel(key.getName());
            c.setAmount(map.get(key));
            p.add(c);
        }
        Collections.sort(p);
        for (Catsort obj : p) {
            w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
        }
        w.write("\n\n");
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
