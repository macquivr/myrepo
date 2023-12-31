package com.example.demo.reports;

import com.example.demo.bean.*;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StypeRepository;
import com.example.demo.utils.DataUtils;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


public class InOutReport implements ReportI {
    private final Repos repos;
    public InOutReport(Repos r) {
        repos = r;
    }

    public void go(FileWriter w, SessionDTO session) throws Exception
    {
        StypeRepository sr = repos.getStypeRepository();
        Stype transfert = sr.findByName("Transfer");

        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data  = ld.filterByDate(session,null,null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();

        printPeriod(w,dates);

        DataUtils du = new DataUtils(repos);
        HashMap<Lenum, Data> dmap = du.populateDmap(session,dates);

        printInOut(w,dmap, transfert);
    }


    private Ion InOutNet(List<Ledger> data, Stype transfert) {
        Ion ret = new Ion();

        for (Ledger l : data) {
            Stype s = l.getStype();
            if (s.getId() != transfert.getId()) {
                if (l.getAmount() > 0)
                    ret.setIn(ret.getIn() + l.getAmount());
                if (l.getAmount() < 0)
                    ret.setOut(ret.getOut() + l.getAmount());
            } else {
                if (l.getAmount() > 0)
                    ret.setTIn(ret.getTIn() + l.getAmount());
                if (l.getAmount() < 0)
                    ret.setTOut(ret.getTOut() + l.getAmount());
            }
        }
        ret.setNet(ret.getIn() + ret.getOut());

        return ret;
    }

    private IonL makeList(HashMap<Lenum, Data> data, Stype transfert) {
        IonL l = new IonL();

        Data d = data.get(Lenum.MAINSAVE);

        Ion ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);

        l.add(ion);

        d = data.get(Lenum.MAIN);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.MORTG);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.SLUSH);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.ANNUAL);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        d = data.get(Lenum.ML);

        ion = InOutNet(d.getLdata(),transfert);
        ion.setData(d);
        l.add(ion);

        return l;
    }
    private void printInOut(FileWriter w, HashMap<Lenum, Data> data, Stype transfert) throws Exception
    {
        IonL l = makeList(data,transfert);

        w.write("               " + l.getColumInLabel() + l.getColumOutLabel() + l.getColumnTInLabel() + l.getColumnTOutLabel() + l.getColumnTnetLabel() + " Fbal\n");
        List<Ion> dl = l.getData();
        double tf = 0;
        for (Ion ion : dl) {
            Data d = ion.getData();
            w.write(d.getLabel() + l.getInLabel(ion) + " " + l.getOutLabel(ion) + " " + l.getTInLabel(ion) + " " + l.getTOutLabel(ion) + " " + l.getTnetLabel(ion));
            if (d.getStmt() != null) {
                w.write(" " + d.getStmt().getFbalance());
                tf += d.getStmt().getFbalance();
            }
            w.write("\n");
        }
        tf = Utils.convertDouble(tf);
        Ion ion = l.getTotal();
        w.write("Total         " + l.getInLabel(ion) + " " + l.getOutLabel(ion) + " " + l.getTInLabel(ion) + " " + l.getTOutLabel(ion) + " " + l.getTnetLabel(ion) + " " + tf);
        w.write("\n");
    }

    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

}
