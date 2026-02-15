package com.example.demo.reports;

import com.example.demo.actions.BudgetSetAction;
import com.example.demo.bean.GBean;
import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MiscGReport implements ReportI {
    private final Repos repos;
    private MRBeanl bdata;
    private MRBeanl cdata;

    public MiscGReport(Repos r) {
        this.repos = r;
        bdata = new MRBeanl();
        cdata = new MRBeanl();
    }

    public String go(FileWriter w, SessionDTO session) throws Exception {
        StartStop ds = new StartStop(session.getStart(), session.getStop());
        w.write("Start: " + ds.getStart().toString() + " Stop:  " + ds.getStop().toString() + "\n");

        if (session.getConsolidate() == Consolidate.MONTHLY) {
            doMonthly(w,ds);
            return null;
        }


        GBean data = calculate(ds);
        print(w,data);
        return null;
    }

    private void doMonthly(FileWriter w, StartStop ds) throws Exception {
        boolean done = false;
        LocalDate curStart = ds.getStart();
        LocalDate curStop = null;
        List<GBean> datal = new ArrayList<GBean>();
        do {
            curStop = curStart.plusMonths(1).minusDays(1);
            StartStop nds = new StartStop(curStart, curStop);

            GBean data = calculate(nds);
            datal.add(data);
            curStart = curStart.plusMonths(1);
            if (curStart.isAfter(ds.getStop())) {
                done = true;
            }
        } while (!done);
        GBean l = null;
        GBean c = new GBean();
        for (GBean g : datal) {
            c.add(g);
            l = g;
        }
        c.setBalance(l.getBalance());
        print(w,c);
    }
    private GBean calculate(StartStop ds) throws Exception {
        GBean ret = new GBean();

        double workIn = 0;
        double nonWorkIn = 0;
        double totalOut = 0;
        double creditOut = 0;
        double fees = 0;
        double free = 0;
        double unpaid = 0;
        double utils = 0;
        double bnet = 0;
        double balance = 0;
        double annual = 0;

        StatementsRepository ssrepo = repos.getStatementsRepository();
        StatementRepository srepo = repos.getStatementRepository();
        LtypeRepository ltRepo = repos.getLtypeRepository();
        StypeRepository stRepo = repos.getStypeRepository();

        LedgerRepository lrepo = repos.getLedgerRepository();
        LabelRepository lblRepo = repos.getLabelRepository();
        BudgetValuesRepository brepo = repos.getBudgetValuesRepository();

        Budgetvalues vb = brepo.findByName("Total");
        List<Budgets> bl = repos.getBudgetsRepository().findAllByBdateBetweenAndBidOrderByBdateAsc(ds.getStart(), ds.getStop(), vb);

        Statements ss = ssrepo.findByStmtdate(ds.getStart());

        Ltype usaa = ltRepo.findById(8).get();
        Ltype capone = ltRepo.findById(7).get();
        Ltype amazon = ltRepo.findById(9).get();
        Stype annuals = stRepo.findById(6).get();

        Statement ustmt = srepo.findAllByStatementsAndLtype(ss, usaa);
        Statement cstmt = srepo.findAllByStatementsAndLtype(ss, capone);
        Statement astmt = srepo.findAllByStatementsAndLtype(ss, amazon);
        List<Statement> bs = getStmts(ss, srepo);

        Label lbl2 = lblRepo.findById(10288);
        Label lbl3 = lblRepo.findById(10428);
        Label lbl4 = lblRepo.findById(13149);
        Label lbl5 = lblRepo.findById(10409);
        Label utilL = lblRepo.findById(10344);

        List<Ledger> alist = lrepo.findAllByTransdateBetweenAndStypeOrderByTransdateAsc(ds.getStart(), ds.getStop(), annuals);
        List<Ledger> freeUsaa = lrepo.findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(), lbl2);
        List<Ledger> freeCapone = lrepo.findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(), lbl3);
        List<Ledger> freeAaa = lrepo.findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(), lbl4);
        List<Ledger> freeCiti = lrepo.findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(), lbl5);

        List<Ledger> credita = lrepo.findAllByTransdateBetweenAndAmountGreaterThanOrderByTransdateAsc(ds.getStart(), ds.getStop(), 0);
        List<Ledger> debta = lrepo.findAllByTransdateBetweenAndAmountLessThanOrderByTransdateAsc(ds.getStart(), ds.getStop(), 0);
        List<Ledger> credit = filter(credita);
        List<Ledger> debt = filter(debta);
        List<Ledger> util = lrepo.findAllByTransdateBetweenAndLabelOrderByTransdateAsc(ds.getStart(), ds.getStop(), utilL);

        double pusaa = 0;
        double pcapone = 0;
        double pamazon = 0;

        for (Ledger l : debt) {
            if (l.getStype().getId() != 8) {
                totalOut += l.getAmount();
            }
            if (l.getLabel().getId() == 11209) {
                creditOut += l.getAmount();
                pusaa += l.getAmount();
            }
            if (l.getLabel().getId() == 10264) {
                creditOut += l.getAmount();
                pcapone += l.getAmount();
            }
            if (l.getLabel().getId() == 10019) {
                creditOut += l.getAmount();
                pamazon += l.getAmount();
            }
            if (l.getLabel().getId() == 12933) {
                creditOut += l.getAmount();
            }
            if (l.getLabel().getCategory().getId() == 14) {
                fees += l.getAmount();
            }
        }

        unpaid += (ustmt.getSbalance() + pusaa);
        unpaid += (cstmt.getSbalance() + pcapone);
        unpaid += (astmt.getSbalance() + pamazon);

        fees -= ustmt.getFee();
        fees -= cstmt.getFee();
        fees -= astmt.getFee();

        for (Ledger l : freeUsaa) {
            free += l.getAmount();
        }
        for (Ledger l : freeCapone) {
            free += l.getAmount();
        }
        for (Ledger l : freeAaa) {
            free += l.getAmount();
        }
        for (Ledger l : freeCiti) {
            free += l.getAmount();
        }

        for (Ledger l : util) {
            utils += l.getAmount();
        }

        for (Ledger l : alist) {
            annual += l.getAmount();
        }

        for (Budgets l : bl) {
            bnet += l.getNet();
        }

        totalOut = Utils.convertDouble(totalOut);

        for (Ledger l : credit) {
            if (l.getLabel().getId() == 12448)
                workIn += l.getAmount();
            else {
                if (l.getStype().getId() != 8) {
                    nonWorkIn += l.getAmount();
                }
            }
        }

        for (Statement s : bs) {
            balance += s.getFbalance();
        }
        ret.setTotalOut(totalOut);
        ret.setWorkIn(workIn);
        ret.setNonworkIn(nonWorkIn);
        ret.setCreditOut(creditOut);
        ret.setUtils(utils);
        ret.setFees(fees);
        ret.setFree(free);
        ret.setUnpaid(unpaid);
        ret.setBudgetNet(bnet);
        ret.setBalance(balance);
        ret.setAnnual(annual);
        return ret;
    }
    private void print(FileWriter w, GBean data) throws Exception {

        w.write("\n");
        w.write("WorkIn: " + data.getWorkIn() + "\n");
        w.write("OtherIn: " + data.getNonWorkIn() + "\n");
        w.write("Total In: " + data.getTotalIn() + "\n");
        w.write("\n");

        w.write("Credit Out: " + data.getCreditOut() + "\n");
        w.write("Utilities: " + data.getUtils() + "\n");
        w.write("Annual: " + data.getAnnual() + "\n");
        w.write("Total Out: " + data.getTotalOut() + "\n");

        w.write("\n");
        w.write("Actual Net: " + data.getTotalNet() + "\n\n");

        w.write("\n");
        w.write("Fees: " + data.getFees() + "\n");
        w.write("Free: " + data.getFree() + "\n");
        w.write("N: " + data.getNfeeFree() + "\n");
        w.write("Unpaid: " + data.getUnpaid() + "\n");
        w.write("BudgetNet: " + data.getBudgetNet() + "\n");
        w.write("Balance: " + data.getBalance() + "\n");
    }

    private List<Statement> getStmts(Statements ss, StatementRepository srepo) {
        List<Statement> ret = new ArrayList<Statement>();

        List<Statement> ustmt = srepo.findAllByStatements(ss);
        for (Statement s : ustmt) {
            if ((s.getLtype().getId() == 3) ||
                    (s.getLtype().getId() == 5) ||
                    (s.getLtype().getId() == 6) ||
                    (s.getLtype().getId() == 11) ||
                    (s.getLtype().getId() == 12) ||
                    (s.getLtype().getId() == 14)) {
                ret.add(s);
            }
        }
        return ret;
    }

    private List<Ledger> filter(List<Ledger> data) {
        List<Ledger> ret = new ArrayList<Ledger>();
        for (Ledger l : data) {
            if ((l.getLtype().getId() == 3) ||
                    (l.getLtype().getId() == 5) ||
                    (l.getLtype().getId() == 6) ||
                    (l.getLtype().getId() == 11) ||
                    (l.getLtype().getId() == 12) ||
                    (l.getLtype().getId() == 14)) {
                ret.add(l);
            }
        }
        return ret;
    }
}
