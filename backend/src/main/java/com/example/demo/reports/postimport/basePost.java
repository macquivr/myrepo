package com.example.demo.reports.postimport;

import com.example.demo.domain.Statements;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StatementsRepository;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

public class basePost {
    protected final Repos repos;
    protected Statements stmts;
    protected boolean ok = false;

    protected FileWriter w;

    public basePost(Repos repos, int sid) {
        this.repos = repos;
        StatementsRepository stmtsRepo = repos.getStatementsRepository();

        Optional<Statements> oss = stmtsRepo.findById(sid);
        if (!oss.isPresent()) {
            System.out.println("No.");
            return;
        }
        stmts = oss.get();
        this.ok = true;
    }

    protected void close()
    {
        try {
            w.flush();
            w.close();
        } catch (Exception ex) {
            // ignore
        }
    }
    protected void printPeriod() throws Exception {
        this.w.write("Period: " + stmts.getStmtdate().toString());
        this.w.write("\n");
    }

    protected void makeWriter(LocalDate dt, String label) {
        String fdir = System.getenv("IMPORT_REPORT");
        if (fdir == null) {
            this.ok = false;
            return;
        }
        int year = dt.getYear();
        String ystr = String.valueOf(year);

        String path = fdir + "/" + ystr;
        File f = new File(path);
        if (!f.exists()) {
            boolean b = f.mkdirs();
            if (!b) {
                this.ok = false;
                return;
            }
        }
        Month m = dt.getMonth();
        String mstr = m.toString();
        path = fdir + "/" + ystr + "/" + mstr;
        f = new File(path);
        if (!f.exists()) {
            boolean b = f.mkdirs();
            if (!b) {
                this.ok = false;
                return;
            }
        }

        path = fdir + "/" + ystr + "/" + mstr + "/" + label;
        try {
            this.w = new FileWriter(path);
        } catch (Exception ex) {
            this.ok = false;
        }
    }
}
