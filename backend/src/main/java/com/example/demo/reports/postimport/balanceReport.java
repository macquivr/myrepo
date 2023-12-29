package com.example.demo.reports.postimport;

import com.example.demo.bean.Ion;
import com.example.demo.bean.tables.InOutTable;
import com.example.demo.importer.Repos;
import com.example.demo.utils.rutils.BalanceUtils;

import java.time.LocalDate;
import java.util.List;

public class balanceReport extends basePost {

    public balanceReport(Repos repos, int sid) {
        super(repos, sid);
    }

    public boolean go() throws Exception
    {
        if (!this.ok) {
            return false;
        }

        BalanceUtils uobj = new BalanceUtils(repos);
        LocalDate dt = stmts.getStmtdate();
        makeWriter(dt,"Balance");
        if (!this.ok) {
            System.out.println("Could not make writer....");
            return false;
        }
        List<Ion> idata = uobj.makeTable(stmts.getId());

        printPeriod();

        InOutTable t = new InOutTable();
        t.populateTable(idata);
        t.Print(w);

        w.write("\n");

        close();
        return true;
    }

}
