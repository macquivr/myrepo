package com.example.demo.reports;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.utils.LData;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class RetireReport implements ReportI {
    private final Repos repos;

    public RetireReport(Repos r) {
        repos = r;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception
    {
        LData ld = new LData(repos.getLedgerRepository());
        LtypeRepository lt = repos.getLtypeRepository();

        Ltype managed = lt.findByName("MLR");
        ld.filterByDate(session,null,managed);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);

        double amt = 0;

        amt += p(w,session,ld,lt,"MLR","Operating Account ");
        amt += p(w,session, ld, lt, "ManagedML", "Managed           ");
        amt += p(w, session, ld, lt,"QIra", "Q 401K            ");
        amt += p(w, session, ld, lt, "StephIra", "Steph 401K        ");
        amt += p(w, session, ld, lt, "QRoth", "Q Roth            ");
        amt += p(w, session, ld, lt, "StephRoth", "Steph Roth        ");
        amt +=  p(w, session, ld, lt, "NetNumberIra", "NetNumber 401K    ");
        amt += p(w, session, ld, lt, "CSB","Local             ");

        w.write("TOTAL:            " + amt);
        w.write("\n");
    }

    private double p(FileWriter w, SessionDTO session, LData ld,  LtypeRepository lt, String name, String str) throws Exception
    {
        Ltype managed = lt.findByName(name);
        List<Ledger> data = ld.filterByDate(session,null,managed);

        Ledger l = data.get(0);
        double amt = l.getAmount();
        w.write(str + amt + "\n");

        return amt;
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

}
