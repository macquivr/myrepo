package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.Checks;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Stype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StypeRepository;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InReport implements ReportI {
    private Repos repos = null;
    private MRBeanl bdata = null;
    private MRBeanl cdata = null;

    public InReport(Repos r) {
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
        StypeRepository sr = repos.getStypeRepository();
        Stype transfert = sr.findByName("Transfer");

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);

        printIn(w,data,transfert);
    }
    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }
    private void printIn(FileWriter w,List<Ledger> data, Stype transfert) throws Exception
    {
        HashMap<Integer, Ledger> hmap = new HashMap<Integer, Ledger>();

        for (Ledger l : data) {
            Stype s = l.getStype();
            if (s.getId() == transfert.getId())
                continue;
            if (l.getAmount() < 0)
                continue;
            Ledger m = hmap.get(l.getLabel().getId());
            if (m == null) {
                hmap.put(l.getLabel().getId(), l);
            } else {
                m.setAmount(Utils.convertDouble(m.getAmount() + l.getAmount()));
            }
        }
        double total = 0;

        Set<Integer> keys = hmap.keySet();
        for (Integer key : keys) {
            Ledger l = hmap.get(key);
            w.write(l.getLabel().getName() + " " + l.getAmount());
            w.write("\n");
            total += l.getAmount();
        }
        w.write("TOTAL: " + Utils.convertDouble(total));
        w.write("\n");
    }


}
