
package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.BVNRowDTO;
import com.example.demo.dto.ui.BVNTableDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.utils.BData;
import com.example.demo.utils.BSData;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idata.BNIData;
import com.example.demo.utils.idata.BNSIData;
import com.example.demo.utils.idata.BVIData;
import com.example.demo.utils.idata.BVSIData;
import com.example.demo.utils.uidata.BNSUI;
import com.example.demo.utils.uidata.BNUI;
import com.example.demo.utils.uidata.BVSUI;
import com.example.demo.utils.uidata.BVUI;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class CReport implements ReportI {
    private static final Integer[] USAA_FREE = { 10288, 10428, 11490 };
    private static final Integer[] CAPONE_FREE = { 10288, 10428, 11490 };
    private static final Integer[] AAA_FREE = { 13149, 11769 };
    private static final Integer[] AMAZON_FREE = { 0 };


    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public CReport(Repos r) {
        repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public double pc()
    {
        return 0.0;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception {
        StatementRepository sr = repos.getStatementRepository();

        LtypeRepository lrepo = repos.getLtypeRepository();
        Ltype ltypeUsaa = lrepo.findByName("Usaa");
        Ltype ltypeCapone = lrepo.findByName("CapitalOne");
        Ltype ltypeAaa = lrepo.findByName("Aaa");
        Ltype ltypeAmazon = lrepo.findByName("Amazon");

        CRBeanl crl = new CRBeanl();
        CRBean busaa = new CRBean("Usaa");
        CRBean bcapone = new CRBean("Capone");
        CRBean baaa = new CRBean("Aaa");
        CRBean bamazon = new CRBean("Amazon");

        StartStop dates = new StartStop(session.getStart(),session.getStop());
        StatementsRepository srepo = repos.getStatementsRepository();
        List<Statements> sts = srepo.findAllByStmtdateBetween(dates.getStart(),dates.getStop());

        HashMap<String,Double> cusaa = new HashMap<String,Double>();
        HashMap<String,Double> ccapone = new HashMap<String,Double>();
        HashMap<String,Double> caaa = new HashMap<String,Double>();
        HashMap<String,Double> camazon = new HashMap<String,Double>();

        for (Statements s : sts) {
            doStmt(dates,s,ltypeUsaa,busaa,12057, USAA_FREE,cusaa);
            doStmt(dates,s,ltypeCapone,bcapone,10265, CAPONE_FREE,ccapone);
            doStmt(dates,s,ltypeAaa,baaa,10076, AAA_FREE,caaa);
            doStmt(dates,s,ltypeAmazon,bamazon,10304, AMAZON_FREE,camazon);
        }

        crl.add(busaa);
        crl.add(bcapone);
        crl.add(baaa);
        crl.add(bamazon);

        crl.Print(w);

        w.write("\n");
        printCategories(w,session,"Usaa",cusaa);
        w.write("\n");
        printCategories(w,session,"Capone",ccapone);
        w.write("\n");
        printCategories(w,session,"Aaa",caaa);
        w.write("\n");
        printCategories(w,session,"Aamzon",camazon);
        w.write("\n");

        /*
        check2(w,ltypeUsaa);
        check2(w,ltypeCapone);
        check2(w,ltypeAaa);
        check2(w,ltypeAmazon);
        */
    }

    private void doStmt(StartStop dates, Statements s, Ltype ltype, CRBean bean, int pd, Integer[] fa,HashMap<String, Double> map) {
        boolean ret = false;
        StatementRepository srepo = repos.getStatementRepository();
        LedgerRepository lrepo = repos.getLedgerRepository();

        Statement sts = srepo.findAllByStatementsAndLtype(s,ltype);
        if (sts == null) {
            return;
        }


        double fee = sts.getFee();
        bean.addOuta(sts.getOuta());
        bean.addFee(fee);

        double pda = 0;
        List<Ledger> ldata = lrepo.findAllByStatement(sts);
        populateCat(map,ldata);
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : ldata) {
            if (l.getLabel().getId() == pd) {
                pda += l.getAmount();
                death.add(l);
            }
        }
        ldata.removeAll(death);
        pda = Utils.convertDouble(pda);
        double free = freeValueD(ldata,ltype.getId(),fa,true);

        double oc = 0;
        for (Ledger l : ldata) {
            if (l.getAmount() > 0) {
                oc += l.getAmount();
            }
        }
        oc = Utils.convertDouble(oc);

        bean.addPd(pda);
        bean.addFree(free);
        bean.addOc(oc);

        bean.calculateDiff(sts.getSbalance(),sts.getIna());
        bean.addNet(Utils.convertDouble(free - fee));
    }

    private void populateCat(HashMap<String,Double> map, List<Ledger> data) {
        for (Ledger l : data) {
            String lstr = null;
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
                d = Utils.convertDouble(d.doubleValue() + l.getAmount().doubleValue());
                map.put(lstr, d);
            }
        }
    }

    private void printCategories(FileWriter w,SessionDTO session, String label, HashMap<String, Double> map) throws Exception
    {
        w.write("\n");
        w.write("By Category " + label + "\n");

        List<Catsort> sort = new Vector<Catsort>();

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String akey = Utils.getAkey(key,map);

            Double d = map.get(key);
            Catsort c = new Catsort();
            c.setLabel(akey);
            c.setAmount(d.doubleValue());
            sort.add(c);
        }
        Collections.sort(sort);
        for (Catsort c : sort)
            w.write(c.getLabel() + " " + c.getAmount() + "\n");
    }

    private void check2(FileWriter w, Ltype ltype) throws Exception {
        StatementRepository sr = repos.getStatementRepository();
        List<Statement> lu = sr.findAllByLtypeOrderByStatements(ltype);

        boolean status = true;
        for (Statement s : lu) {
            if ((s.getStatements().getId() == 96) || (s.getStatements().getId() == 1))
                continue;
            double ob = Utils.convertDouble(s.getSbalance() - s.getIna());
            double fb = Utils.convertDouble(ob + s.getOuta() + s.getFee());
            if (fb != s.getFbalance()) {
                w.write("No " + s.getId() + " " + s.getStatements().getName() + " " + fb + " " + s.getFbalance() + " " + ob + "\n");
                status = false;
            } else {
                //w.write("Ok " + s.getId() + " " + s.getStatements().getName() + "\n");
            }
        }
        if (status) {
            w.write("Ok.\n");
        }
    }
    public void go3(FileWriter w, SessionDTO session) throws Exception {
        StatementRepository sr = repos.getStatementRepository();

        LtypeRepository lrepo = repos.getLtypeRepository();
        Ltype ltypeUsaa = lrepo.findByName("Usaa");
        Ltype ltypeCapone = lrepo.findByName("CapitalOne");
        Ltype ltypeAaa = lrepo.findByName("Aaa");
        Ltype ltypeAmazon = lrepo.findByName("Amazon");

        List<Statement> lu = sr.findAllByLtypeOrderByStatements(ltypeUsaa);
        boolean checku = check(w,lu);
        if (checku) {
            w.write("Ok Usaa\n");
        }

        lu = sr.findAllByLtypeOrderByStatements(ltypeCapone);
        checku = check(w,lu);
        if (checku) {
            w.write("Ok Capone\n");
        }

        lu = sr.findAllByLtypeOrderByStatements(ltypeAaa);
        checku = check(w,lu);
        if (checku) {
            w.write("Ok Aaa\n");
        }

        lu = sr.findAllByLtypeOrderByStatements(ltypeAmazon);
        checku = check(w,lu);
        if (checku) {
            w.write("Ok Amazon\n");
        }

    }

    public boolean check(FileWriter w, List<Statement> lu ) throws Exception
    {
        boolean f = true;
        double v = 0;
        boolean status = true;
        Statement sts = null;
        for (Statement s : lu) {
            if (s.getStatements().getId() == 96)
                sts = s;
        }
        lu.remove(sts);
        for (Statement s : lu) {
            if (f) {
                v = s.getFbalance();
                f = false;
            } else {
                if (s.getSbalance() != v) {
                    w.write("eEK " + s.getId() + " expected " + v + " Found " + s.getSbalance() + "\n");
                    status = false;
                }
            }
            v = s.getFbalance();
        }
        return status;
    }
    public void go4(FileWriter w, SessionDTO session) throws Exception {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data = ld.filterByDate(session, null, null);

        StartStop dates = ld.getDates();

        printPeriod(w, dates);
        w.write("\n");

        w.write("Free:\n");
        double free = printFree(w,session, ld);
        w.write("\n");

        w.write("Fee:\n");
        double fee = printFee(w,session, ld,dates);
        w.write("\n\n");

        double fnet = free - fee;
        if (fnet != 0) {
            w.write("FNET: " + Utils.convertDouble(fnet) + "\n\n");
        }
        printUn(w,session,ld,dates);
    }

    private double printFree(FileWriter w,SessionDTO session, LData ld) throws Exception {
        double tfree = 0;

        List<Ledger> data = ld.filterByDate(session, null, null);
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if ((l.getLtype().getId() < 7) || (l.getLtype().getId() > 10))
                death.add(l);
        }
        data.removeAll(death);

        tfree += freeUsaa(w,data);
        tfree += freeCapone(w,data);
        tfree += freeAaa(w,data);
        tfree += freeAmazon(w,data);

        if (tfree > 0) {
            tfree = Utils.convertDouble(tfree);
            w.write("TOTAL FREE: " + tfree + "\n");
        }

        return tfree;
    }
    private double printFee(FileWriter w,SessionDTO session, LData ld, StartStop dates) throws Exception {
        double total = 0;
        double usaa = 0;
        double capone = 0;
        double aaa = 0;
        double amazon = 0;

        StatementsRepository stmtsRepo = repos.getStatementsRepository();
        List<Statements> stmts = stmtsRepo.findAllByStmtdateBetween(dates.getStart(),dates.getStop());

        StatementRepository srepo = repos.getStatementRepository();
        LtypeRepository lrepo = repos.getLtypeRepository();
        Ltype ltypeUsaa = lrepo.findByName("Usaa");
        Ltype ltypeCapone = lrepo.findByName("CapitalOne");
        Ltype ltypeAaa = lrepo.findByName("Aaa");
        Ltype ltypeAmazon = lrepo.findByName("Amazon");

        for (Statements s : stmts) {
            usaa += findFee(srepo,s,ltypeUsaa);
            capone += findFee(srepo,s,ltypeCapone);
            aaa += findFee(srepo,s,ltypeAaa);
            amazon += findFee(srepo,s,ltypeAmazon);
        }
        total = usaa + capone + aaa + amazon;

        if (usaa > 0) {
            w.write("USAA: " + Utils.convertDouble(usaa) + "\n");
        }
        if (capone > 0) {
            w.write("Capone: " + Utils.convertDouble(capone) + "\n");
        }
        if (aaa > 0) {
            w.write("Aaa: " + Utils.convertDouble(aaa) + "\n");
        }
        if (amazon > 0) {
            w.write("Amazon: " + Utils.convertDouble(amazon) + "\n");
        }
        if (total > 0) {
            w.write("Total Fee: " + Utils.convertDouble(total));
        }
        return total;
    }

    private double findFee(StatementRepository srepo, Statements s, Ltype ltype) {
        Statement stmt = srepo.findAllByStatementsAndLtype(s, ltype);
        if (stmt == null) {
            System.out.println("Could not find " + s.getName() + " " + s.getId() + " " + ltype.getName());
            return 0.0;
        }
        return stmt.getFee();
    }

    private double findUnpaid(SessionDTO session, LData ld, StatementRepository srepo, Statements s, Ltype ltype,int label, boolean over)  throws Exception {
        Statement stmt = srepo.findAllByStatementsAndLtype(s, ltype);
        if (stmt == null) {
            return 0.0;
        }
        double balance = stmt.getSbalance();
        List<Ledger> data = ld.filterByDate(session, null, ltype);
        double pd = 0;
        double t = 0;
        for (Ledger l : data) {
            if ((l.getAmount() > 0) && (l.getStatement().getStatements().getId() == s.getId())) {
                t += l.getAmount();
            }
            if ((l.getLabel().getId() == label) &&
                    (l.getStatement().getStatements().getId() == s.getId())) {
                pd += l.getAmount();
            }
        }
        pd = Utils.convertDouble(pd);
        if (balance == pd) {
            return 0.0;
        }
        double ret = Utils.convertDouble(balance - pd);
        if (over) {
            if (ret < 0)
                return ret;
        } else {
            if (ret > 0) {
                return ret;
            }
        }
        return 0.0;
    }

    private void printUn(FileWriter w,SessionDTO session, LData ld, StartStop dates) throws Exception {
        printUnpaid(w,session,ld,dates,true);
        printUnpaid(w,session,ld,dates,false);
    }
    private void printUnpaid(FileWriter w,SessionDTO session, LData ld, StartStop dates,boolean over) throws Exception {
        double total = 0;
        double usaa = 0;
        double capone = 0;
        double aaa = 0;
        double amazon = 0;

        StatementsRepository stmtsRepo = repos.getStatementsRepository();
        List<Statements> stmts = stmtsRepo.findAllByStmtdateBetween(dates.getStart(),dates.getStop());

        StatementRepository srepo = repos.getStatementRepository();
        LtypeRepository lrepo = repos.getLtypeRepository();
        Ltype ltypeUsaa = lrepo.findByName("Usaa");
        Ltype ltypeCapone = lrepo.findByName("CapitalOne");
        Ltype ltypeAaa = lrepo.findByName("Aaa");
        Ltype ltypeAmazon = lrepo.findByName("Amazon");

        for (Statements s : stmts) {
            usaa += findUnpaid(session, ld, srepo, s, ltypeUsaa, 12057,over);
            capone += findUnpaid(session, ld,srepo,s,ltypeCapone,10265,over);
            aaa += findUnpaid(session, ld,srepo,s,ltypeAaa,10076,over);
            amazon += findUnpaid(session, ld, srepo,s,ltypeAmazon,10304,over);
        }

        total = usaa + capone + aaa + amazon;

        if (total != 0) {
            if (over) {
                w.write("Over:\n");
            } else {
                w.write("UnPaid:\n");
            }
        }

        if (usaa != 0) {
            w.write("USAA: " + Utils.convertDouble(usaa) + "\n");
        }
        if (capone != 0) {
            w.write("Capone: " + Utils.convertDouble(capone) + "\n");
        }
        if (aaa != 0) {
            w.write("Aaa: " + Utils.convertDouble(aaa) + "\n");
        }
        if (amazon != 0) {
            w.write("Amazon: " + Utils.convertDouble(amazon) + "\n");
        }
        if (total > 0) {
            w.write("Total Unpaid: " + Utils.convertDouble(total));
        }

    }
    private double freeValue(List<Ledger> data, int ltype, Integer[] fv) {
        return freeValueD(data,ltype,fv,false);
    }
    private double freeValueD(List<Ledger> data, int ltype, Integer[] fv,boolean d) {
        double ret = 0;
        List<Ledger> death = new Vector<Ledger>();
        List<Integer> lv = Arrays.asList(fv);
        for (Ledger l : data) {
            if (l.getLtype().getId() != ltype)
                continue;
            if (lv.contains(l.getLabel().getId())) {
                ret += l.getAmount();
                death.add(l);
            }
        }
        if (d) {
            data.removeAll(death);
        }
        return Utils.convertDouble(ret);
    }

    private double freeUsaa(FileWriter w, List<Ledger> data) throws Exception  {
        double value = freeValue(data,8,USAA_FREE);

        if (value > 0) {
            w.write("USAA: " + Utils.convertDouble(value) + "\n");
        }
        return value;
    }
    private double freeCapone(FileWriter w, List<Ledger> data) throws Exception {
        double value = freeValue(data,7,CAPONE_FREE);

        if (value > 0) {
            w.write("CAPONE: " + Utils.convertDouble(value) + "\n");
        }

        return value;
    }
    private double freeAaa(FileWriter w, List<Ledger> data) throws Exception {
        double value = freeValue(data,10, AAA_FREE);

        if (value > 0) {
            w.write("AAA: " + Utils.convertDouble(value) + "\n");
        }

        return value;
    }
    private double freeAmazon(FileWriter w, List<Ledger> data) throws Exception {
        double value =  freeValue(data,9, AMAZON_FREE);

        if (value > 0) {
            w.write("AMAZON: " + Utils.convertDouble(value) + "\n");
        }

        return value;
    }
    private void pSpent(FileWriter w, HashMap<String, Double> vmap,int m, BVNRowDTO svalues, BVNRowDTO snet) {
        MRBeanl budgetl = new MRBeanl();

        double utilsValue = vmap.get("Utils").doubleValue() * m;
        MRBean utils = new MRBean("Utils", svalues.getUtils(), utilsValue, snet.getUtils());
        budgetl.add(utils);

        double usaaValue = vmap.get("Usaa").doubleValue() * m;
        MRBean usaa = new MRBean("Usaa", svalues.getUsaa(), usaaValue, snet.getUsaa());
        budgetl.add(usaa);

        double caponeValue = vmap.get("Capone").doubleValue() * m;
        MRBean capone = new MRBean("Capone", svalues.getCapone(), caponeValue, snet.getCapone());
        budgetl.add(capone);

        double aaaValue = vmap.get("Aaa").doubleValue() * m;
        MRBean aaa = new MRBean("Aaa", svalues.getAaa(), aaaValue, snet.getAaa());
        budgetl.add(aaa);

        double amazonValue = vmap.get("Amazon").doubleValue() * m;
        MRBean amazon = new MRBean("Amazon", svalues.getAmazon(), amazonValue, snet.getAmazon());
        budgetl.add(amazon);

        budgetl.Print(w);
    }
    private void pIn(FileWriter w, List<Ledger> data) throws Exception {
        List<Ledger> death = new Vector<Ledger>();
        double work = 0;
        double other = 0;
        double tin = 0;
        double out = 0;
        double net = 0;
        for (Ledger l : data) {
            if (l.getAmount() < 0) {
                out += l.getAmount();
            } else {
                death.add(l);
                if (l.getLabel().getId() == 12448)
                    work += l.getAmount();
                else {
                    other += l.getAmount();
                }
            }
        }
        data.removeAll(death);
        net = Utils.convertDouble(work + other + out);
        tin = Utils.convertDouble(work + other);

        w.write("In Work: " + Utils.convertDouble(work) + "\n");
        if (other > 0) {
            w.write("In Other: " + Utils.convertDouble(other) + "\n");
        }
        w.write("In: " + tin + "\n");
        w.write("Out: " + Utils.convertDouble(out) + "\n");
        w.write("Net: " + net + "\n");
        w.write("\n");
    }
    private void prest(FileWriter w, List<Ledger> data) throws Exception {
        boolean r = false;
        for (Ledger l : data) {
             r = true;
             w.write(l.getLabel().getName() + " " + l.getAmount() + " " + l.getStype().getName() + "\n");
        }
        if (!r) {
            w.write("\n***\n");
        }
    }

    private void potherStype(FileWriter w, List<Ledger> data, String label) throws Exception {
        List<Ledger> death = new Vector<Ledger>();
        double amt = 0;
        int cnt=0;
        for (Ledger l : data) {
            if ((l.getAmount() < 0) && (l.getStype().getName().equals(label))){
                death.add(l);
                amt += l.getAmount();
                cnt++;
            }
        }
        if (cnt > 0) {
            w.write(label + ": " + Utils.convertDouble(amt) + "\n");
        }
        data.removeAll(death);
    }

    private void noTransfer(List<Ledger> data) {
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Transfer"))
                death.add(l);
        }
        data.removeAll(death);
    }

    private void pbills(FileWriter w,List<Ledger> data) throws Exception{
        w.write("Bills:\n");

        double total = 0;
        int cnt = 0;
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Bills")) {
                death.add(l);
                w.write(l.getLabel().getName() + " " + l.getAmount() + "\n");
                cnt++;
                total += l.getAmount();
            }
        }
        data.removeAll(death);
        if (cnt > 1) {
            w.write("Total: " + Utils.convertDouble(total) + "\n");
        }
        w.write("\n");
    }
    private MRBean validateDog(FileWriter w, int m, HashMap<String, Double> map, double rlvalues, double rlnet, List<Ledger> data) throws Exception {
        double value = map.get("Dog").doubleValue() * m;

        double other = 0;
        double p = 0;
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (isDog(l))   {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad Dog rl " + p + " " + rlvalues);
            return null;
        }

        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad Dog rlnet " + n + " " + rlnet);
            return null;
        }

        return new MRBean("Dog", p, value, n);
    }

    private MRBean validateStuff(FileWriter w, int lid, int m, String bstr, HashMap<String, Double> map, double rlvalues, double rlnet, List<Ledger> data) throws Exception {
        double value = map.get(bstr).doubleValue() * m;

        double other = 0;
        double p = 0;
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (l.getLabel().getId() == lid)  {
                if (l.getLtype().getId() != 3) {
                    other += l.getAmount();
                }
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad " + bstr + " rl " + p + " " + rlvalues);
            return null;
        }

        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad " + bstr + " rlnet " + p + " " + rlnet);
            return null;
        }

        MRBean ret = new MRBean(bstr, p, value, n);
        ret.setOther(other);
        return ret;
    }

    private MRBean validatePos(FileWriter w, int m, Double mapv, double rlvalues, double rlsvalues, double rlnet, double rlsnet,List<Ledger> data) throws Exception {
        double value = mapv.doubleValue() * m;
        double p = 0;
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("POS")) {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad pos rl " + p + " " + rlvalues);
            return null;
        }

        if (p != rlsvalues) {
            w.write("Bad pos rls " + p + " " + rlsvalues);
            return null;
        }
        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad pos rlnet " + p + " " + rlnet);
            return null;
        }

        if (n != rlsnet) {
            w.write("Bad pos rlsnet " + p + " " + rlsnet);
            return null;
        }

        return new MRBean("POS",p, value, n);
    }

    private MRBean validateAtm(FileWriter w, int m, Double mapv, double rlvalues, double rlsvalues, double rlnet, double rlsnet,List<Ledger> data) throws Exception {
        double value = mapv.doubleValue() * m;
        double p = 0;
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("ATM")) {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad atm rl " + p + " " + rlvalues);
            return null;
        }

        if (p != rlsvalues) {
            w.write("Bad atm rls " + p + " " + rlsvalues);
            return null;
        }
        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad atm rlnet " + p + " " + rlnet);
            return null;
        }

        if (n != rlsnet) {
            w.write("Bad atm rlsnet " + p + " " + rlsnet);
            return null;
        }

        return new MRBean("ATM",p, value ,n);
    }
    public BVNTableDTO doBnet(SessionDTO session, HashMap<String, Integer> map) {
        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<BVNRowDTO>();
        ret.setBvn(rdata);

        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();

        BNIData ldata = new BNIData(bndata);
        BNUI bobj = new BNUI(map);
        bobj.go(session, dates, ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBsnet(SessionDTO session,HashMap<String, Integer> map) {
        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<BVNRowDTO>();
        ret.setBvn(rdata);

        BNSIData ldata = new BNSIData(bndata);
        BNSUI bobj = new BNSUI(map);
        bobj.go(session, dates, ldata, rdata);

        return ret;
    }

    private HashMap<String, Integer> getMap() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(),b.getId());
        }
        return map;
    }

    private HashMap<String, Double> getMapV() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String,Double> map = new HashMap<String, Double>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(),b.getValue());
        }
        return map;
    }

    public BVNTableDTO doBvalues(SessionDTO session, HashMap<String, Integer> map) {
        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<BVNRowDTO>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVIData ldata = new BVIData(bvdata);
        BVUI bobj = new BVUI(map);
        bobj.go(session, dates, ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBvs(SessionDTO session, HashMap<String, Integer> map) {
        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<BVNRowDTO>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVSIData ldata = new BVSIData(bvdata);
        BVSUI bobj = new BVSUI(map);
        bobj.go(session, dates, ldata, rdata);

        return ret;
    }

    public void go2(FileWriter w, SessionDTO session) throws Exception
    {
        double tout = 0;
        bdata = new MRBeanl();
        cdata = new MRBeanl();

        StypeRepository sr = repos.getStypeRepository();
        Stype misc = sr.findByName("Misc");
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,misc,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (isDog(l) || (l.getAmount() > 0)) {
                death.add(l);
            }
        }
        data.removeAll(death);
        consolidateMisc(data);
        printMisc(w,data);
    }
    private boolean isDog(Ledger l) {
        if (l.getChecks() != null) {
            Checks c = l.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                return true;
            }
        }
        if (l.getLabel().getId() == 13137)
            return true;

        return false;
    }
    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

    private void consolidateMisc(List<Ledger> data) {
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : data) {
            if (!(l.getStype().getName().equals("Misc")))
                continue;
            for (Ledger inner : data) {
                if (!(l.getStype().getName().equals("Misc")))
                    continue;
                if (death.contains(inner))
                    continue;
                if (inner == l)
                    continue;
                if (inner.getChecks() != null) {
                    if (l.getChecks() == null)
                        continue;
                    Payee pl = l.getChecks().getPayee();
                    Payee pi = inner.getChecks().getPayee();
                    if (pl == pi) {
                        death.add(inner);
                        l.setAmount(Utils.convertDouble(l.getAmount() + inner.getAmount()));
                    }
                } else {
                    if (l.getChecks() != null)
                        continue;
                    if (l.getLabel() == inner.getLabel()) {
                        death.add(inner);
                        l.setAmount(Utils.convertDouble(l.getAmount() + inner.getAmount()));
                    }
                }
            }
        }
        data.removeAll(death);
    }

    private int category(List<Ledger> data, int category) {
        int ret = 0;

        for (Ledger l : data) {
            if (!(l.getStype().getName().equals("Misc")))
                continue;
            if (l.getLabel().getCategory().getId() == category)
                ret++;
        }
        return ret;
    }
    private void printMisc(FileWriter w, List<Ledger> data) throws Exception {
        w.write("MISC:\n");
        double total = 0;
        CategoryRepository cr = repos.getCategoryRepository();
        List<Category> crl = cr.findAll();
        List<Ledger> death = new Vector<Ledger>();
        for (Category c : crl) {
            int f = category(data, c.getId());
            double t = 0;
            if (f > 0) {
                w.write("CATEGORY: " + c.getName() + "\n");
                int cnt = 0;
                for (Ledger l : data) {
                    if (!(l.getStype().getName().equals("Misc")))
                        continue;
                    if (l.getLabel().getCategory() == c) {
                        cnt++;
                        death.add(l);
                        t += l.getAmount();
                        if (l.getChecks() != null) {
                            w.write(l.getTransdate().toString() + " " + l.getChecks().getPayee().getCheckType().getName() + " " + l.getChecks().getPayee().getName() + " " + l.getAmount() + "\n");
                        } else {
                            w.write(l.getTransdate().toString() + " " + l.getLabel().getName() + " " + l.getAmount() + "\n");
                        }
                    }
                }
                total += t;
                if (cnt > 1) {
                    w.write("Total: " + Utils.convertDouble(t) + "\n\n");
                } else {
                    w.write("\n");
                }
            }
        }
        w.write("Misc Total: " + Utils.convertDouble(total));
        data.removeAll(death);
    }


}
