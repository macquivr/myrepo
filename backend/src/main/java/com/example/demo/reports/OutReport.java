package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Stype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StypeRepository;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class OutReport implements ReportI {
    private final Repos repos;

    public OutReport(Repos r) {
        this.repos = r;
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

        printOut(w,data,transfert);
    }
    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }
    private void printOut(FileWriter w,List<Ledger> data, Stype transfert) throws Exception
    {
        HashMap<OutMapKey, Lvd> hmap = new HashMap<>();

        for (Ledger l : data) {
            Stype s = l.getStype();
            if (s.getId() == transfert.getId())
                continue;
            if (l.getAmount() > 0)
                continue;
            OutMapKey key = OutMapKey.makeKey(l);

            Lvd m = hmap.get(key);
            if (m == null) {
                Lvd obj = key.makeLvd(l);
                hmap.put(key, obj);
            } else {
                m.setValue(Utils.convertDouble(m.getValue() + l.getAmount()));
            }
        }
        double total = 0;

        total += ptype(w,hmap,OutMapKeyType.STATICTYPE);
        w.write("****\n");
        total += ptype(w,hmap,OutMapKeyType.ANNUAL);
        w.write("****\n");
        total += ptype(w,hmap,OutMapKeyType.PAYEE);
        w.write("****\n");
        total += ptype(w,hmap,OutMapKeyType.NAME);

        w.write("TOTAL: " + Utils.convertDouble(total));
        w.write("\n");
    }

    private double ptype(FileWriter w,  HashMap<OutMapKey, Lvd> hmap, OutMapKeyType type) throws Exception {
        double total = 0;
        Set<OutMapKey> keys = hmap.keySet();
        List<Lvd> lst = new ArrayList<>();
        for (OutMapKey key : keys) {
            if (key.getType() == type) {
                Lvd l = hmap.get(key);
                lst.add(l);
                total += l.getValue();
            }
        }
        if (type == OutMapKeyType.STATICTYPE) {
            OutMapStaticKey.fillInMissing(lst,hmap);
        }
        Collections.sort(lst);
        for (Lvd obj : lst) {
            w.write(obj.getLabel() + " " + obj.getValue());
            w.write("\n");
        }
        total = Utils.convertDouble(total);
        w.write("TOTAL: " + total);
        w.write("\n");
        return total;
    }

}
