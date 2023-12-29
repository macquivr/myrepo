package com.example.demo.utils.rutils;

import com.example.demo.bean.Ion;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.repository.StatementRepository;
import com.example.demo.repository.StatementsRepository;
import com.example.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BalanceUtils {
    private Repos repos;

    public BalanceUtils(Repos r) {
        this.repos = r;
    }

    public List<Ion> makeTable(int stmt) {
        StatementsRepository stmtsRepo = repos.getStatementsRepository();
        Optional<Statements> so = stmtsRepo.findById(stmt);
        if (!so.isPresent())
            return null;
        Statements stmts = so.get();

        Statement main = getStmt(stmts, 3);
        Statement mainsave = getStmt(stmts, 5);
        Statement mortg = getStmt(stmts, 6);
        Statement slush = getStmt(stmts, 12);
        Statement annual = getStmt(stmts, 14);
        Statement ml = getStmt(stmts, 11);

        List<Ion> ldata = new ArrayList<Ion>();
        Ion maino = new Ion();
        setData(maino,main,"Main");
        ldata.add(maino);

        Ion mainsaveo = new Ion();
        setData(mainsaveo,mainsave, "MainSave");
        ldata.add(mainsaveo);

        Ion mortgo = new Ion();
        setData(mortgo,mortg, "Mortg");
        ldata.add(mortgo);

        Ion slusho = new Ion();
        setData(slusho,slush,"Slush");
        ldata.add(slusho);

        Ion annualo = new Ion();
        setData(annualo,annual, "Annual");
        ldata.add(annualo);

        Ion mlo = new Ion();
        setData(mlo,ml, "ML");
        ldata.add(mlo);

        return ldata;
    }

    private void setData(Ion data, Statement stmt,String label) {
        data.setLabel(label);
        data.setIn(stmt.getIna());
        data.setOut(stmt.getOuta());
        data.setNet(Utils.convertDouble(data.getIn() - data.getOut()));
        data.setBalance(stmt.getFbalance());
    }

    private Statement getStmt(Statements stmt, int ltype) {
        Ltype l = getLtype(ltype);
        StatementRepository stmtRepo = repos.getStatementRepository();
        return stmtRepo.findAllByStatementsAndLtype(stmt, l);

    }
    private Ltype getLtype(int ltype) {
        LtypeRepository lrepo = repos.getLtypeRepository();
        Optional<Ltype> lo = lrepo.findById(ltype);
        if (!lo.isPresent())
            return null;
        return lo.get();
    }
}
