package com.example.demo.utils;

import com.example.demo.bean.Data;
import com.example.demo.bean.Lenum;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.Statements;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.repository.StatementRepository;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StatementsRepository;

import java.util.HashMap;

public class DataUtils {
    private Repos repos = null;

    public DataUtils(Repos r) { repos = r; }

    public static HashMap<Lenum,Data> makeDmap()
    {
        HashMap<Lenum, Data> data = new HashMap<Lenum,Data>();
        Data dmain = new Data();
        data.put(Lenum.MAIN,dmain);

        Data dmortg = new Data();
        data.put(Lenum.MORTG,dmortg);

        Data dmainsave = new Data();
        data.put(Lenum.MAINSAVE,dmainsave);

        Data dslush = new Data();
        data.put(Lenum.SLUSH,dslush);

        Data dannual = new Data();
        data.put(Lenum.ANNUAL,dannual);

        Data dml = new Data();
        data.put(Lenum.ML,dml);

        return data;
    }

    public HashMap<Lenum, Data> populateDmap(SessionDTO filter, StartStop dates)
    {
        HashMap<Lenum, Data> data = makeDmap();

        LData ld = new LData(repos.getLedgerRepository());

        Data dmain = data.get(Lenum.MAIN);
        Data dmortg = data.get(Lenum.MORTG);
        Data dms = data.get(Lenum.MAINSAVE);
        Data dslush = data.get(Lenum.SLUSH);
        Data dannual = data.get(Lenum.ANNUAL);
        Data dml = data.get(Lenum.ML);

        LtypeRepository ltyper = repos.getLtypeRepository();
        dmain.setLtype(ltyper.findByName("Main Account"));
        dmortg.setLtype(ltyper.findByName("Mortgage Account"));
        dms.setLtype(ltyper.findByName("Main Savings"));
        dslush.setLtype(ltyper.findByName("SlushFund"));
        dannual.setLtype(ltyper.findByName("Annual Account"));
        dml.setLtype(ltyper.findByName("Merrill Lynch"));

        dmain.setLdata(ld.filterByDate(filter,null,dmain.getLtype()));
        dmortg.setLdata(ld.filterByDate(filter,null,dmortg.getLtype()));
        dms.setLdata(ld.filterByDate(filter,null,dms.getLtype()));
        dslush.setLdata(ld.filterByDate(filter,null,dslush.getLtype()));
        dannual.setLdata(ld.filterByDate(filter,null,dannual.getLtype()));
        dml.setLdata(ld.filterByDate(filter,null,dml.getLtype()));

        StatementsRepository repo = repos.getStatementsRepository();
        Statements stmts = repo.findByStmtdate(dates.getStart());

        StatementRepository stmtRepo = repos.getStatementRepository();
        if (dmain.getLdata().size() > 0)
            dmain.setStatement(stmtRepo.findAllByIdAndLtype(dmain.getLdata().get(dmain.getLdata().size()-1).getStatement().getId(),dmain.getLtype()));

        if (dmortg.getLdata().size() > 0)
            dmortg.setStatement(stmtRepo.findAllByIdAndLtype(dmortg.getLdata().get(dmortg.getLdata().size()-1).getStatement().getId(),dmortg.getLtype()));

        if (dms.getLdata().size() > 0)
            dms.setStatement(stmtRepo.findAllByIdAndLtype(dms.getLdata().get(dms.getLdata().size()-1).getStatement().getId(),dms.getLtype()));

        if (dslush.getLdata().size() > 0)
            dslush.setStatement(stmtRepo.findAllByIdAndLtype(dslush.getLdata().get(dslush.getLdata().size()-1).getStatement().getId(),dslush.getLtype()));
        else {
            if (stmts != null) {
                dslush.setStatement(stmtRepo.findAllByStatementsAndLtype(stmts, dslush.getLtype()));
            }
        }

        if (dannual.getLdata().size() > 0)
            dannual.setStatement(stmtRepo.findAllByIdAndLtype(dannual.getLdata().get(dannual.getLdata().size()-1).getStatement().getId(),dannual.getLtype()));

        if (dml.getLdata().size() > 0)
            dml.setStatement(stmtRepo.findAllByIdAndLtype(dml.getLdata().get(dml.getLdata().size()-1).getStatement().getId(),dml.getLtype()));

        return data;
    }
}
