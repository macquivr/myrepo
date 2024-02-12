package com.example.demo.reports.postimport;

import com.example.demo.bean.Catsort;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statement;
import com.example.demo.domain.Stype;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.repository.StatementRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.utils.Utils;
import com.example.demo.utils.rutils.InUtils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class inReport extends basePost {

    public inReport(Repos repos, int sid) {
        super(repos, sid);
    }

    public boolean go() throws Exception
    {
        if (!this.ok) {
            return false;
        }

        LocalDate dt = stmts.getStmtdate();
        makeWriter(dt,"In");
        if (!this.ok) {
            System.out.println("Could not make writer....");
            return false;
        }
        InUtils obj = new InUtils();
        List<Ledger> ldata = getData();
        obj.go(ldata);

        List<Catsort> data = obj.getData();
        data.sort(Collections.reverseOrder());
        double totalo = obj.getTotalo();

        printPeriod();

        print(w,data,totalo);
        w.write("\n");

        close();
        return true;
    }

    private List<Ledger> getData() {
        List<Ledger> ret = new ArrayList<>();
        StypeRepository sr = repos.getStypeRepository();
        Stype transfert = sr.findByName("Transfer");

        HashMap<Integer, Ledger> hmap = new HashMap<>();
        List<Ledger> data = getLData();

        for (Ledger l : data) {
            Stype s = l.getStype();
            if (s.getId() == transfert.getId())
                continue;
            if (l.getAmount() < 0)
                continue;
            Ledger m = hmap.get(l.getLabel().getId());
            if (m == null) {
                ret.add(l);
                hmap.put(l.getLabel().getId(), l);
            } else {
                m.setAmount(Utils.convertDouble(m.getAmount() + l.getAmount()));
            }
        }

        return ret;
    }

    private List<Ledger> getLData() {
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
    private void print(FileWriter w, List<Catsort> data, double totalo) throws Exception
    {
        for (Catsort c : data) {
            double a = c.getAmount();
            String l = c.getLabel();
            w.write(l + " " + a);
            w.write("\n");
        }
        w.write("TOTAL: " + Utils.convertDouble(totalo));
        w.write("\n");
    }

}
