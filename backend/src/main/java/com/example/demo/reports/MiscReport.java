package com.example.demo.reports;

import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class MiscReport implements ReportI {
    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public MiscReport(Repos r) {
        repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public double pc()
    {
        return 0.0;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception
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
        print(w,data);
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
            for (Ledger inner : data) {
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
            if (l.getLabel().getCategory().getId() == category)
                ret++;
        }
        return ret;
    }
    private void print(FileWriter w, List<Ledger> data) throws Exception {
        CategoryRepository cr = repos.getCategoryRepository();
        List<Category> crl = cr.findAll();

        for (Category c : crl) {
            int f = category(data, c.getId());
            double t = 0;
            if (f > 0) {
                w.write("CATEGORY: " + c.getName() + "\n");
                for (Ledger l : data) {
                    if (l.getLabel().getCategory() == c) {
                        t += l.getAmount();
                        if (l.getChecks() != null) {
                            w.write(l.getTransdate().toString() + " " + l.getChecks().getPayee().getCheckType().getName() + " " + l.getChecks().getPayee().getName() + " " + l.getAmount() + "\n");
                        } else {
                            w.write(l.getTransdate().toString() + " " + l.getLabel().getName() + " " + l.getAmount() + "\n");
                        }
                    }
                }
                w.write("Total: " + Utils.convertDouble(t) + "\n\n");
            }
        }

    }


}
