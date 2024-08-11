
package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.*;

public class CReport implements ReportI {
    private static final Integer[] USAA_FREE = { 10288, 10428, 11490 };
    private static final Integer[] CAPONE_FREE = { 10288, 10428, 11490 };
    private static final Integer[] AAA_FREE = { 13149, 11769 };
    private static final Integer[] AMAZON_FREE = { 0 };


    private final Repos repos;

    public CReport(Repos r) {
        repos = r;
    }

    public String go(FileWriter w, SessionDTO session) throws Exception {
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

        HashMap<String,Double> cusaa = new HashMap<>();
        HashMap<String,Double> ccapone = new HashMap<>();
        HashMap<String,Double> caaa = new HashMap<>();
        HashMap<String,Double> camazon = new HashMap<>();

        for (Statements s : sts) {
            doStmt(s,ltypeUsaa,busaa,12057, USAA_FREE,cusaa);
            doStmt(s,ltypeCapone,bcapone,10265, CAPONE_FREE,ccapone);
            doStmt(s,ltypeAaa,baaa,10076, AAA_FREE,caaa);
            doStmt(s,ltypeAmazon,bamazon,10304, AMAZON_FREE,camazon);
        }

        crl.add(busaa);
        crl.add(bcapone);
        crl.add(baaa);
        crl.add(bamazon);

        crl.Print(w);

        w.write("\n");
        printCategories(w,"Usaa",cusaa);
        w.write("\n");
        printCategories(w,"Capone",ccapone);
        w.write("\n");
        printCategories(w,"Aaa",caaa);
        w.write("\n");
        printCategories(w,"Aamzon",camazon);
        w.write("\n");

        return null;
    }

    private void doStmt( Statements s, Ltype ltype, CRBean bean, int pd, Integer[] fa,HashMap<String, Double> map) {
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
        List<Ledger> death = new Vector<>();
        for (Ledger l : ldata) {
            if (l.getLabel().getId() == pd) {
                pda += l.getAmount();
                death.add(l);
            }
        }
        ldata.removeAll(death);
        pda = Utils.convertDouble(pda);
        double free = freeValueD(ldata,ltype.getId(),fa);

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
            String lstr;
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
    }

    private void printCategories(FileWriter w, String label, HashMap<String, Double> map) throws Exception
    {
        w.write("\n");
        w.write("By Category " + label + "\n");

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

    private double freeValueD(List<Ledger> data, int ltype, Integer[] fv) {
        double ret = 0;
        List<Ledger> death = new Vector<>();
        List<Integer> lv = Arrays.asList(fv);
        for (Ledger l : data) {
            if (l.getLtype().getId() != ltype)
                continue;
            if (lv.contains(l.getLabel().getId())) {
                ret += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        return Utils.convertDouble(ret);
    }


}
