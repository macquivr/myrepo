package com.example.demo.actions;


import com.example.demo.bean.MlBean;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.BadDataException;
import com.example.demo.importer.Repos;
import com.example.demo.importer.checkUtil;
import com.example.demo.repository.*;
import com.example.demo.utils.mydate.DUtil;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class MlBalanceAction extends BaseAction implements ActionI{
    private static final Logger ilog = LoggerFactory.getLogger(MlBalanceAction.class);

    private List<MlBean> data;
    private double mlr = 0;
    private double managed = 0;
    private double qira = 0;
    private double stephira = 0;
    private double qroth = 0;
    private double stephroth = 0;
    private LocalDate stmtDate = null;
    private Statements stmts = null;

    public MlBalanceAction(Repos r) {
        super(r);
        data = new ArrayList<MlBean>();
    }

    public boolean go(SessionDTO session) throws Exception {
        String text = readPdf();
        List<String> lines = makeLines(text);
        getDate(lines);
        List<String> nlines = adjust(lines);
        process(nlines);
        makeData();
        save();

        return true;
    }

    private void md(double amt, int lid) {
        LtypeRepository lrepo = repos.getLtypeRepository();
        MlBean mlrb = new MlBean();
        Optional<Ltype> mlrltypeo = lrepo.findById(lid);
        Ltype lt = mlrltypeo.get();
        mlrb.setAmt(amt);
        mlrb.setStatements(this.stmts);
        mlrb.setLocalDate(this.stmtDate);
        mlrb.setLtype(lt);
        this.data.add(mlrb);
    }
    private void makeData() {
        StatementsRepository srepo = repos.getStatementsRepository();
        LtypeRepository lrepo = repos.getLtypeRepository();

        this.stmts = srepo.findByStmtdate(this.stmtDate);
        md(this.mlr,15);
        md(this.managed,16);
        md(this.stephira,17);
        md(this.qira,18);
        md(stephroth,19);
        md(qroth,20);
    }

    private void save() {
         for (MlBean b : data) {
             Statement stmt = saveStmt(b);
             saveLedger(b,stmt);
         }
    }

    private Statement saveStmt(MlBean b) {
        LedgerRepository lrepo = repos.getLedgerRepository();
        StatementRepository srepo = repos.getStatementRepository();

        Statement s = new Statement();
        s.setStatements(b.getStmts());
        s.setLtype(b.getLtype());
        LocalDate start = b.getDate();
        LocalDate stop = start.plusMonths(1);

        List<Ledger> lst = lrepo.findAllByAmountAndTransdateBetweenOrderByTransdateAsc(2703.6,start,stop);
        srepo.save(s);

        return s;
    }

    private void saveLedger(MlBean b, Statement stmt) {
        LedgerRepository lrepo = repos.getLedgerRepository();
        LabelRepository lblRepo = repos.getLabelRepository();
        Label lbl = lblRepo.findById(12753);
        StypeRepository srepo = repos.getStypeRepository();
        Optional<Stype> stypeo = srepo.findById(5);
        Stype stype = stypeo.get();

        Ledger l = new Ledger();
        l.setAmount(b.getAmt());
        l.setTransdate(b.getDate());
        l.setChecks(null);
        l.setLabel(lbl);
        l.setLtype(b.getLtype());
        l.setStatement(stmt);
        l.setStype(stype);

        lrepo.save(l);
    }
    private String adjustDate(String dstr) {
        String str = dstr.trim();
        int idx = str.indexOf(',');
        String rest = str.substring(idx);
        return  str.substring(0,3) + " 01" + rest;
    }
    private void getDate(List<String> lines) {
        for (String s : lines) {
            if ((s.startsWith("YOUR MERRILL LYNCH REPORT") ||
			    (s.startsWith("WEALTH MANAGEMENT REPORT")))) {
                int idx = s.indexOf('-');
                String rest = s.substring(idx+1);
                String dstr = adjustDate(rest);
                stmtDate = DUtil.getDate(dstr,DUtil.ML_FMT);
                return;
            }
        }
    }

    private double makeNum(String str) {
        int idx = str.indexOf(' ');
        String rest = str.substring(idx+1).trim();
        idx = rest.lastIndexOf(' ');
        String num = rest.substring(0,idx).replaceAll(",","");
        return Double.parseDouble(num);
    }
    private double calculateMlr(String line) {
        int idx = line.indexOf('*');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }

    private double calculateManaged(String line) {
        int idx = line.indexOf('*');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }
    private double calculateQIra(String line) {
        int idx = line.lastIndexOf('E');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }
    private double calculateStephIra(String line) {
        int idx = line.lastIndexOf('E');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }
    private double calculateQRoth(String line) {
        int idx = line.lastIndexOf('E');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }
    private double calculateStephRoth(String line) {
        int idx = line.lastIndexOf('E');
        String rest = line.substring(idx+1).trim();
        return makeNum(rest);
    }

    private void process(List<String> lines) {
        for (String line : lines) {
            if (line.contains("13165")) {
                this.mlr = calculateMlr(line);
            }
            if (line.contains("13195")) {
                this.managed = calculateManaged(line);
            }
            if (line.contains("13196")) {
                this.qira = calculateQIra(line);
            }
            if (line.contains("13197")) {
                this.stephira = calculateStephIra(line);
            }
            if (line.contains("13166")) {
                this.qroth = calculateQRoth(line);
            }
            if (line.contains("13167")) {
                this.stephroth = calculateStephRoth(line);
            }
        }

    }


    private List<String> adjust(List<String> lines) {
        List<String> ret = new ArrayList<String>();
        for (String line : lines) {
            if (line.contains("CREDIT & LENDING"))
                return ret;
            if (line.contains("OPERATING ACCOUNT 7DH-13165 CMA*") ||
                    line.contains("NVESTMENT ACCOUNT 7DH-13195 CMA/APERIO S&P 500 TAX LOSS HARVES*") ||
                    line.contains("JOHN IRA 7DH-13196 IRA/CIO MODERATE ETF SIZE & STYLE") ||
                    line.contains("STEPHANIE IRA 7DH-13197 IRA/CIO MODERATE ETF SIZE & STYLE") ||
                    line.contains("JOHN ROTH IRA 7DH-13166 RRA/CIO AGGRESSIVE ETF CORE") ||
                    line.contains("STEPHANIE ROTH IRA 7DH-13167 RRA/CIO AGGRESSIVE ETF CORE")) {
                ret.add(line);
            }
        }
        return ret;
    }
    private List<String>  makeLines(String text)
    {
        List<String> ret = new Vector<>();
        StringTokenizer st = new StringTokenizer(text,"\n");
        while (st.hasMoreTokens()) {
            String l = st.nextToken();
            ret.add(l);
        }
        return ret;
    }
    private String readPdf() throws BadDataException {
        String fname = "ml.pdf";
        System.out.println("Reading " + fname);

        PDFParser parser;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        PDFTextStripper pdfStripper;

        String parsedText;
        File file = new File(checkUtil.getObj(false).getDir() + "/" + fname);

        if (!file.exists()) {
            throw new BadDataException("NO FILE " + file.getPath());
        }
        try {
            parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            throw new BadDataException(e.getMessage());
        } finally {
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return parsedText;
    }

}
