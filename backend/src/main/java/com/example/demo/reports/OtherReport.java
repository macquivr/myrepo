
package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class OtherReport implements ReportI {

    private final Repos repos;

    public OtherReport(Repos r) {
        this.repos = r;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception {


        LtypeRepository lrepo = repos.getLtypeRepository();
        Ltype ltypeMs = lrepo.findByName("Main Savings");
        Ltype ltypeSlush = lrepo.findByName("SlushFund");
        Ltype ltypeMortg = lrepo.findByName("Mortgage Account");
        Ltype ltypeMl = lrepo.findByName("Merrill Lynch");
        Ltype ltypeAnnual = lrepo.findByName("Annual Account");

        LData ldata = new LData(repos.getLedgerRepository());

        List<Ledger> ms = ldata.filterByDate(session,null,ltypeMs);
        List<Ledger> slush = ldata.filterByDate(session,null,ltypeSlush);
        List<Ledger> mortg = ldata.filterByDate(session,null,ltypeMortg);
        List<Ledger> ml = ldata.filterByDate(session,null,ltypeMl);
        List<Ledger> annual = ldata.filterByDate(session,null,ltypeAnnual);

        LData ld = new LData(repos.getLedgerRepository());
        ld.filterByDate(session,null,null);
        printPeriod(w, ld.getDates());

        P(w,ms,"Main Savings");
        P(w,slush,"Rainy Day");
        P(w,mortg,"Mortgage");
        P(w,ml,"Merrill Lynch");
        P(w,annual,"Annual");
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }
    private void P(FileWriter w, List<Ledger> data, String label) throws Exception {
        w.write("\n" + label + "\n");
        double ina = 0;
        double outa = 0;
        double net;

        HashMap<Label, Double> inmap = new HashMap<>();
        HashMap<Label, Double> outmap = new HashMap<>();
        HashMap<Label, Double> tmp;
        HashMap<Payee, Double> checkmap = new HashMap<>();

        for (Ledger l : data) {
            if (l.getAmount() > 0) {
                tmp = inmap;
                ina += l.getAmount();
            } else {
                tmp = outmap;
                outa += l.getAmount();
                if (l.getLabel().getId() == 10312) {
                    Payee p = l.getChecks().getPayee();

                    Double d = checkmap.get(p);
                    if (d == null) {
                        checkmap.put(p, l.getAmount());
                    } else {
                        double nd = Utils.convertDouble(d + l.getAmount());
                        checkmap.put(p, nd);
                    }
                }
            }
            Double d = tmp.get(l.getLabel());
            if (d == null) {
                tmp.put(l.getLabel(), l.getAmount());
            } else {
                double nd = Utils.convertDouble(d + l.getAmount());
                tmp.put(l.getLabel(), nd);
            }
        }
        Set<Label> keysi = inmap.keySet();
        if (!keysi.isEmpty()) {
            w.write("\n  In: " + ina + " \n");
            for (Label lbl : keysi) {
                Double dv = inmap.get(lbl);
                w.write("    " + lbl.getName() + " " + dv + "\n");
            }
        }

        Set<Label> keyso = outmap.keySet();
        if (!keyso.isEmpty()) {
            w.write("\n  Out: " + outa + "\n");
            for (Label lbl : keyso) {
                Double dv = outmap.get(lbl);
                w.write("    " + lbl.getName() + " " + dv + "\n");
                if (lbl.getId() == 10312) {
                    Set<Payee> ps = checkmap.keySet();
                    for (Payee p : ps) {
                        w.write("      " + p.getName() + " " + checkmap.get(p) + "\n");
                    }
                }
            }
        }

        if ((ina > 0) && (outa > 0)) {
            net = Utils.convertDouble(ina - outa);
            w.write("  Net: " + net + "\n");
        }
    }
}
