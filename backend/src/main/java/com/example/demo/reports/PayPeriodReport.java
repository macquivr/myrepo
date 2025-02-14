package com.example.demo.reports;

import com.example.demo.bean.AllP;
import com.example.demo.bean.CatSortWithLabels;
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
    public String go(FileWriter w, SessionDTO session) throws Exception {
        boolean firstHalf = true;
        boolean month = false;

        Consolidate c = session.getConsolidate();
        if (c.equals(Consolidate.HALF)) {
            firstHalf = false;
        }
        if (c.equals(Consolidate.MONTHLY)) {
            month = true; 
        }

        LocalDate start = session.getStart();
        LocalDate stop = session.getStop();
        PayperiodRepository pr = repos.getPayPeriod();
        WdatamapRepository wr = repos.getPayWdatamap();

        List<Payperiod> ppl = pr.findAllByStartBetweenOrderByStartAsc(start,stop);
        if ((ppl == null) || (ppl.isEmpty())) {
            return "Couldn't find period....";
        }
        List<Wdatamap> whodata = getWhoData(w, ppl, firstHalf, month,wr,session);

        Payperiod pp1 = null;
        Payperiod pp2 = null;

        if (month) {
            pp1 = ppl.get(0);
            pp2 = ppl.get(1);
        } else {
            pp1 = (firstHalf) ? ppl.get(0) : ppl.get(1);
        }

        double targetOut = pp1.getOuta().getOutr() + ((pp2 != null) ? pp2.getOuta().getOutr() : 0);
        double targetCOut = pp1.getOuta().getOutc() + ((pp2 != null) ? pp2.getOuta().getOutc() : 0);
        double targetIn = pp1.getIna().getTotal() + ((pp2 != null) ? pp2.getIna().getTotal() : 0);
        double cfreeIn = pp1.getIna().getCreditfree() + ((pp2 != null) ? pp2.getIna().getCreditfree() : 0);
        double crefundIn = pp1.getIna().getRefund() + ((pp2 != null) ? pp2.getIna().getRefund() : 0);

        targetOut = Utils.convertDouble(targetOut);
        targetCOut = Utils.convertDouble(targetCOut);
        targetIn = Utils.convertDouble(targetIn);
        cfreeIn = Utils.convertDouble(cfreeIn);
        crefundIn = Utils.convertDouble(crefundIn);
        double ctargetIn = Utils.convertDouble(cfreeIn + crefundIn);

        w.write("Freq Reg:\n");
        double itotal = pFreq(w,whodata,true,false);
        double ftotal = pFreq(w,whodata,false,false);

        w.write("Freq Credit:\n");
        double citotal = pFreq(w,whodata,true,true);
        double cototal = pFreq(w,whodata,false,true);

        itotal = Utils.convertDouble(itotal);
        citotal = Utils.convertDouble(citotal);
        if (itotal != targetIn) {
            return "Mismatch freq in " + itotal + " expected " + targetIn;
        }
        if (citotal != ctargetIn) {
            return "Mismatch freq Cin " + citotal + " expected " + ctargetIn;
        }

        if (ftotal != targetOut) {
            return "Mismatch freq out " + ftotal + " expected " + targetOut;
        }
        if (cototal != targetCOut) {
            return "Mismatch freq Cout " + cototal + " expected " + targetCOut;
        }

        w.write("Summary\n");
        w.write("Regular:\n");
        peachWho(w,whodata,false,true);
        w.write("\n");
        w.write("OtherItemized:\n");
        pother(w,whodata,false);

        w.write("\n");
        w.write("Credit\n");
        peachWho(w,whodata,true,true);

        w.write("\n");
        w.write("Details\n");
        w.write("Regular:\n");
        peachWho(w,whodata,false,false);

        w.write("\n");

        w.write("Credit\n");
        peachWho(w,whodata,true,false);

        w.write("OtherDetails\n");
        peachOther(w,whodata,false);

        return null;
    }

    private List<Wdatamap> getWhoData(FileWriter w, List<Payperiod> ppl, boolean firstHalf, boolean month, WdatamapRepository wr, SessionDTO session) throws Exception {
        Payperiod pp = (firstHalf) ? ppl.get(0) : ppl.get(1);
        Payperiod pp2 = (month) ? ppl.get(1) : null;

        if (month) {
          w.write("Start: " + session.getStart().toString() + " Stop: " + session.getStop().toString() + "\n");
        } else {
          w.write("Start: " + pp.getStart().toString() + " Stop: " + pp.getStop().toString() + "\n");
        }

        List<Wdatamap> whodata = wr.findAllByWidOrderByWho(pp);
        if (month) {
          List<Wdatamap> whodata2 = wr.findAllByWidOrderByWho(pp2);
          whodata.addAll(whodata2);
	    }

        pIn(w,pp,pp2);
        w.write("\n");
        pOut(w,pp,pp2);
        w.write("\n");

        pNetA(w,pp,pp2);
        w.write("\n");

       return whodata;
    }

    private void pNetA(FileWriter w, Payperiod pp, Payperiod pp2) throws Exception {
        Intable ina = pp.getIna();
        Outtable outa = pp.getOuta();

        Intable ina2 = (pp2 != null) ? pp2.getIna() : null;
	    Outtable outa2 = (pp2 != null) ? pp2.getOuta() : null;

        double outt = outa.getOutr() + ((outa2 != null) ? outa2.getOutr() : 0);
        double intotal = ina.getTotal() + ((ina2 != null) ? ina2.getTotal() : 0);
        double total = intotal + outt;

        w.write("Net " + Utils.convertDouble(total) + "\n");

    }

    private void pd(FileWriter w, String label, double data) throws Exception {
        if (data != 0) {
            data = Utils.convertDouble(data);
            w.write(label + " " + data + "\n");/* nop */
        }
    }
    private void pIn(FileWriter w, Payperiod pp, Payperiod pp2) throws Exception {
        Intable obj = pp.getIna();
        Intable obj2 = (pp2 != null) ? pp2.getIna() : null;


        pd(w,"Work",obj.getWork() + ((obj2 != null) ? obj2.getWork() : 0));
        pd(w,"MLSale",obj.getMlsale() + ((obj2 != null) ? obj2.getMlsale() : 0));
        pd(w,"MLDividend",obj.getMldividend() + ((obj2 != null) ? obj2.getMldividend() : 0));
        pd(w,"MLIn",obj.getMlin() + ((obj2 != null) ? obj2.getMlin() : 0));
        pd(w,"Arizona",obj.getArizona() + ((obj2 != null) ? obj2.getArizona() : 0));
        pd(w,"Interest",obj.getInterest() + ((obj2 != null) ? obj2.getInterest() : 0));
        pd(w,"Refund",obj.getRefund() + ((obj2 != null) ? obj2.getRefund() : 0));
        pd(w,"MiscIn",obj.getMiscin() + ((obj2 != null) ? obj2.getMiscin() : 0));
        pd(w,"Total",obj.getTotal() + ((obj2 != null) ? obj2.getTotal() : 0));
    }

    private void pOut(FileWriter w, Payperiod pp, Payperiod pp2) throws Exception {
        Outtable obj = pp.getOuta();
        Outtable obj2 = (pp2 != null) ? pp2.getOuta() : null;

        pd(w,"OutR", obj.getOutr() + ((obj2 != null) ? obj2.getOutr() : 0));
        pd(w,"Credit", obj.getOutc() + ((obj2 != null) ? obj2.getOutc() : 0));
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

    private double pFreq(FileWriter w, List<Wdatamap> data, boolean ina, boolean credit) throws Exception {

        double total = 0;
        if (ina) {
            w.write("In\n");
        } else {
            w.write("Out\n");
        }
        double tmp = 0;
        HashMap<Kvp, CatSortWithLabels> map = new HashMap<Kvp, CatSortWithLabels>();
        for (Wdatamap obj : data) {
            if ((obj.getOther() != null) && ((obj.getOther().getId() == 32) || (obj.getOther().getId() == 31)))
                continue;

            if ((credit && (obj.getCredit() == 0)) ||
                    (!credit && (obj.getCredit() == 1)))
                continue;

            double amt = obj.getTid().getAmount();

            if ((ina && (amt > 0)) ||
                    (!ina && (amt < 0))) {

                if (!ina && !credit) {
                    if (obj.getFreq().getId() == 12) {
                        tmp += amt;
                        System.out.println(obj.getTid().getId() + " " + amt + " " + tmp);
                    }
                }
                CatSortWithLabels d = map.get(obj.getFreq());
                if (d == null) {
                    d = new CatSortWithLabels(obj.getTid().getLabel(),amt,obj.getFreq().getId() == 12);
                    map.put(obj.getFreq(), d);
                } else {
                    d.addData(obj.getTid().getLabel(),amt);
                    map.put(obj.getFreq(), d);
                }
            }
        }
        List<CatSortWithLabels> p = new ArrayList<CatSortWithLabels>();
        Set<Kvp> keys = map.keySet();
        for (Kvp key : keys) {
            CatSortWithLabels value = map.get(key);
            value.setLabel(key.getName());
            p.add(value);
        }
        Collections.sort(p);
        for (CatSortWithLabels obj : p) {

            total += obj.getAmount();
            if (!ina && !credit) {
                System.out.println(obj.getLabel() + " AMT: " + obj.getAmount() + " " + total);
            }
            w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
            if (!obj.isOther()) {
                List<Ledger> ld = obj.getData();
                if (ld.size() > 1) {
                    for (Ledger l : ld) {
                        w.write("    " + l.getLabel().getName() + " " + l.getAmount() + "\n");
                    }
                }
            }
        }
        total =  Utils.convertDouble(total);
        System.out.println("TOTAL: " + total);
        w.write("Total: " + total);
        w.write("\n\n");

        return total;
    }

    private void pother(FileWriter w, List<Wdatamap> data, boolean credit) throws Exception {
        double total = 0;
        HashMap<Kvp, Double> map = new HashMap<Kvp, Double>();
        for (Wdatamap obj : data) {
            if ((obj.getWho().getId() != 11) || (obj.getOther() == null))
                continue;
            if ((obj.getOther().getId() == 31)) {
                continue;
            }
            if ((credit && (obj.getCredit() == 1)) ||
                    (!credit && (obj.getCredit() == 0))) {
                double amt  = obj.getTid().getAmount();
                if (amt < 0) {
                    total += amt;
                    Double d = map.get(obj.getOther());
                    if (d == null) {
                      map.put(obj.getOther(), amt);
                    } else {
                        double nd = Utils.convertDouble(d.doubleValue() + amt);
                        map.put(obj.getOther(), nd);
                    }
                }
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

    private void peachOther(FileWriter w, List<Wdatamap> data, boolean credit) throws Exception {
        KvpRepository kr = repos.getKvp();
        Kvp kl = kr.findByName("otherTypes");
        List<Kvp> otherl = kr.findAllByType(kl.getId());
        double total = 0;

        for (Kvp k : otherl) {
            HashMap<Label, Double> map = new HashMap<Label, Double>();
            total = 0;
            for (Wdatamap obj : data) {
              if ((obj.getOther() == null) || (obj.getTid().getAmount() >= 0))
                continue;

              if ((credit && (obj.getCredit() == 1)) ||
                    (!credit && (obj.getCredit() == 0))) {
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
                total += obj.getAmount();
            }
            if (total == 0) { 
        		continue;
            }
            w.write(k.getName() + ":\n");

            for (Catsort obj : p) {
                w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
            }
            total = Utils.convertDouble(total);
            w.write("Total: " + total);
            w.write("\n\n");
        }
    }

    private void peachWho(FileWriter w, List<Wdatamap> data, boolean credit, boolean justTotal) throws Exception {
        KvpRepository kr = repos.getKvp();
        Kvp who = kr.findByName("who");
        List<Kvp> whol = kr.findAllByType(who.getId());
        double total = 0;
        double ftotal = 0;
        for (Kvp k : whol) {
            HashMap<String, Double> map = new HashMap<String, Double>();
            if ((k.getId() == 11) && !justTotal)
                continue;

            total = 0;
            for (Wdatamap obj : data) {
                if ((credit && (obj.getCredit() == 1)) ||
                        (!credit && (obj.getCredit() == 0))) {
                    if (obj.getWho().getId() == k.getId()) {
                        Kvp other = obj.getOther();
                        if ((other == null) || (other.getId() != 31)) {
                            String label = ((other != null) && (other.getId() == 36)) ? "ATM" : obj.getTid().getLabel().getName();
                            Double d = map.get(label);
                            Double amt = (d == null) ? obj.getTid().getAmount() : Utils.convertDouble(d.doubleValue() + obj.getTid().getAmount());
                            map.put(label, amt);
                        }
                    }
                }
            }


            List<Catsort> p = new ArrayList<Catsort>();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                Catsort c = new Catsort();
                c.setLabel(key);
                c.setAmount(map.get(key));
                p.add(c);
            }
            Collections.sort(p);
            for (Catsort obj : p) {;
                if (obj.getAmount() < 0) {
                    total += obj.getAmount();
                }
            }

            total = Utils.convertDouble(total);

            if (total != 0) {
                if (justTotal) {
                    w.write(k.getName() + ": " + total + "\n");
                    ftotal += total;
                } else {
                    w.write(k.getName() + ":\n");
                    for (Catsort obj : p) {
                        w.write(obj.getLabel() + ": " + obj.getAmount() + "\n");
                    }
                    w.write("Total: " + total);
                    w.write("\n\n");
                }
            }
        }
        ftotal = Utils.convertDouble(ftotal);
        w.write("Total: " + ftotal + "\n");
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
