package com.example.demo.reports.postimport;

import com.example.demo.bean.tables.OcTable;
import com.example.demo.dto.ui.CStatusTableDTO;
import com.example.demo.importer.Repos;
import com.example.demo.utils.rutils.OcUtils;

import java.time.LocalDate;

public class ocReport extends basePost {

    public ocReport(Repos repos, int sid) {
        super(repos, sid);
    }

    public boolean go() throws Exception
    {
        if (!this.ok) {
            return false;
        }

        OcUtils uobj = new OcUtils(repos);
        LocalDate dt = stmts.getStmtdate();
        makeWriter(dt,"Oc");
        if (!this.ok) {
            System.out.println("Could not make writer....");
            return false;
        }
        CStatusTableDTO tdata = uobj.makeTableData(dt);

        printPeriod();

        OcTable table = new OcTable();
        table.populateTable(tdata);
        table.Print(this.w);

        close();
        return true;
    }

}
