package com.example.demo.reports;

import com.example.demo.bean.Lvd;
import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CreditReport implements ReportI {
    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public CreditReport(Repos r) {
        repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public void go(FileWriter w, SessionDTO session) throws Exception
    {
        bdata = new MRBeanl();
        cdata = new MRBeanl();

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);

        printUsaa(w,data);
        printCapOne(w,data);
        printAmazon(w,data);
        printAaa(w,data);
    }

    private List<Ledger> getL(List<Ledger> data,int ltype) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : data) {
            if ((l.getLtype().getId() == ltype) && (l.getAmount().doubleValue() < 0))
                ret.add(l);
        }
        return ret;
    }
    private HashMap<Category,Lvd> getMap(List<Ledger> ldata, int ltype) {
        List<Ledger> data = getL(ldata,ltype);
        HashMap<Category, Lvd> map = new HashMap<Category,Lvd>();
        for (Ledger l : data) {
            Category c = l.getLabel().getCategory();
            Lvd d = map.get(c);
            if (d == null) {
                Lvd lv = new Lvd();
                lv.setValue(l.getAmount());
                map.put(c,lv);
            } else {
                double amt = Utils.convertDouble(l.getAmount().doubleValue() + d.getValue().doubleValue());
                d.setValue(Double.valueOf(amt));
            }
        }
        return map;
    }

    private void P(FileWriter w, HashMap<Category,Lvd> map) throws Exception  {
        Set<Category> keys = map.keySet();
        int max = 0;
        for (Category key : keys) {
            String cname = key.getName();
            if (cname.length() > max)
                max = cname.length();
        }
        for (Category key : keys) {
            Lvd l = map.get(key);
            String cname = key.getName();
            int len = max - cname.length();
            String sp = "";
            while (len > 0) {
                sp = sp.concat(" ");
                len--;
            }
            w.write(key.getName() + sp + "\t" + String.valueOf(l.getValue()) + "\n");
        }
    }
    private void printUsaa(FileWriter w, List<Ledger> data) throws Exception  {
        HashMap<Category,Lvd> map = getMap(data,8);

        w.write("Usaa:\n");

        P(w,map);

        w.write("\n\n");
    }

    private void printCapOne(FileWriter w, List<Ledger> data) throws Exception {
        HashMap<Category,Lvd> map = getMap(data,7);

        w.write("CapOne:\n");

        P(w,map);

        w.write("\n\n");
    }

    private void printAmazon(FileWriter w, List<Ledger> data) throws Exception {
        HashMap<Category,Lvd> map = getMap(data,9);

        w.write("Amazon:\n");

        P(w,map);

        w.write("\n\n");
    }

    private void printAaa(FileWriter w, List<Ledger> data) throws Exception  {
        HashMap<Category,Lvd> map = getMap(data,10);

        w.write("Aaa:\n");

        P(w,map);

        w.write("\n\n");
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }



}
