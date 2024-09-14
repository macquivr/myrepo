package com.example.demo.reports.postimport;

import com.example.demo.bean.Catsort;
import com.example.demo.bean.OBean;
import com.example.demo.bean.tables.OutTable;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statement;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.repository.StatementRepository;
import com.example.demo.utils.Utils;
import com.example.demo.utils.rutils.OutUtils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class outReport extends basePost {

    public outReport(Repos repos, int sid) {
        super(repos, sid);
    }

    public boolean go() throws Exception
    {
        if (!this.ok) {
            return false;
        }

        LocalDate dt = stmts.getStmtdate();
        makeWriter(dt,"Out");
        if (!this.ok) {
            System.out.println("Could not make writer....");
            return false;
        }
        OutUtils obj = new OutUtils(repos);
        List<Ledger> ldata = getData();
        obj.go(ldata);

        List<Catsort> data = obj.getData();
        double totalo = obj.getTotalo();

        printPeriod();

        print(w,data,totalo);
        w.write("\n");

        close();
        return true;
    }

    private List<Ledger> getData() {
        List<Ledger> ret = new ArrayList<>();
        LedgerRepository lerepo = repos.getLedgerRepository();
        StatementRepository srepo = repos.getStatementRepository();
        LtypeRepository lrepo = repos.getLtypeRepository();

        Ltype main = lrepo.findByName("Main Account");
        Ltype mainsave = lrepo.findByName("Main Savings");
        Ltype mortg = lrepo.findByName("Mortgage Account");
        Ltype slush = lrepo.findByName("SlushFund");
        Ltype annual = lrepo.findByName("Annual Account");
        Ltype ml = lrepo.findByName("Merrill Lynch");

        Statement smain = srepo.findAllByStatementsAndLtype(stmts,main);
        Statement smainsave = srepo.findAllByStatementsAndLtype(stmts,mainsave);
        Statement smortg = srepo.findAllByStatementsAndLtype(stmts,mortg);
        Statement sslush= srepo.findAllByStatementsAndLtype(stmts,slush);
        Statement sannual = srepo.findAllByStatementsAndLtype(stmts,annual);
        Statement sml = srepo.findAllByStatementsAndLtype(stmts,ml);

        List<Ledger> mainl = lerepo.findAllByStatement(smain);
        List<Ledger> mainsl = lerepo.findAllByStatement(smainsave);
        List<Ledger> mortgl = lerepo.findAllByStatement(smortg);
        List<Ledger> slushl = lerepo.findAllByStatement(sslush);
        List<Ledger> annuall = lerepo.findAllByStatement(sannual);
        List<Ledger> mll = lerepo.findAllByStatement(sml);

        ret.addAll(mainl);
        ret.addAll(mainsl);
        ret.addAll(mortgl);
        ret.addAll(slushl);
        ret.addAll(annuall);
        ret.addAll(mll);

        return ret;
    }

    private void print(FileWriter w, List<Catsort> data, double totalo)
    {
        OutTable ot = new OutTable();
        List<OBean> ob = new Vector<>();

        for (Catsort c : data) {
            double a = c.getAmount();
            double p = a/totalo;
            double percent = Utils.convertDouble(p * 100);
            OBean obj = new OBean();
            obj.setLabel(c.getLabel());
            obj.setAmount(a);
            obj.setPercent(percent);
            ob.add(obj);
        }
        ot.populateTable(ob);

        ot.Print(w);
    }

}
