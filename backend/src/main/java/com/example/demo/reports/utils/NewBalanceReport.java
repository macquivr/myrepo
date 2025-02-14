package com.example.demo.reports.utils;

import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.bean.mr.GLObj;
import com.example.demo.bean.nbBean;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.BVNRowDTO;
import com.example.demo.dto.ui.BVNTableDTO;
import com.example.demo.importer.Repos;
import com.example.demo.reports.ReportI;
import com.example.demo.repository.*;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.BData;
import com.example.demo.utils.BSData;
import com.example.demo.utils.LData;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idata.BNIData;
import com.example.demo.utils.idata.BNSIData;
import com.example.demo.utils.idata.BVIData;
import com.example.demo.utils.idata.BVSIData;
import com.example.demo.utils.uidata.BNSUI;
import com.example.demo.utils.uidata.BNUI;
import com.example.demo.utils.uidata.BVSUI;
import com.example.demo.utils.uidata.BVUI;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class NewBalanceReport implements ReportI {
    private final Repos repos;

    public NewBalanceReport(Repos r) {
        repos = r;
    }
    

    public String go(FileWriter w, SessionDTO session) throws Exception {

        StatementsRepository srepo = repos.getStatementsRepository();
        List<Statements> sdata = srepo.findAll();
        Statements stmt = sdata.get(sdata.size()-1);

        LocalDate start = stmt.getStmtdate();
        LocalDate stop = start.plusMonths(1);

        List<Ledger> mldata = getMlData(start,stop);
        Csbt csbt = getCsbt(start,stop);
        Mlt mlt = getMlt(start,stop);

        List<nbBean> current = getCurrent(w,mldata, csbt,mlt);

        start = start.minusMonths(1);
        stop = stmt.getStmtdate();

        mldata = getMlData(start,stop);
        csbt = getCsbt(start,stop);
        mlt = getMlt(start,stop);

        w.write("\n\n");
        pall(w,mldata, csbt,mlt, current);

        return null;
    }

    private List<nbBean> getCurrent(FileWriter w, List<Ledger> mldata, Csbt csbt, Mlt mlt) throws Exception {
        double mlrd = 0;
        double managedMLd = 0;
        double stephirad = 0;
        double qirad = 0;
        double stephrothd = 0;
        double qrothd = 0;
        double netnumd = 0;
        double total = 0;

        for (Ledger l : mldata) {
            total += l.getAmount();
            int sid = l.getLtype().getId();
            switch (sid) {
                case 15:
                    mlrd = l.getAmount();
                    break;
                case 16:
                    managedMLd = l.getAmount();
                    break;
                case 17:
                    stephirad = l.getAmount();
                    break;
                case 18:
                    qirad = l.getAmount();
                    break;
                case 19:
                    stephrothd = l.getAmount();
                    break;
                case 20:
                    qrothd = l.getAmount();
                    break;
                case 21:
                    netnumd = l.getAmount();
                    break;
            }
        }
        List<nbBean> objs = new ArrayList<nbBean>();
        nbBean mlrb = new nbBean("MLR");
        nbBean managedb = new nbBean("ManagedML");
        nbBean stephirab = new nbBean("StephIra");
        nbBean qirab = new nbBean("QIra");
        nbBean stephrothb = new nbBean("StephRoth");
        nbBean qrothb = new nbBean("QRoth");
        nbBean workirab = new nbBean("WorkIra");
        nbBean csbb = new nbBean("CSB");
        nbBean mltb = new nbBean("MLCash");
        nbBean totalb = new nbBean("Total");

        mlrb.setCurrent(mlrd);
        managedb.setCurrent(managedMLd);
        stephirab.setCurrent(stephirad);
        qirab.setCurrent(qirad);
        stephrothb.setCurrent(stephrothd);
        qrothb.setCurrent(qrothd);
        workirab.setCurrent(netnumd);
        csbb.setCurrent(csbt.getBalance());
        mltb.setCurrent(mlt.getBalance());

        total += csbt.getBalance();
        total += mlt.getBalance();
        total = Utils.convertDouble(total);
        totalb.setCurrent(total);

        objs.add(mlrb);
        objs.add(managedb);
        objs.add(stephirab);
        objs.add(qirab);
        objs.add(stephrothb);
        objs.add(qrothb);
        objs.add(workirab);
        objs.add(csbb);
        objs.add(mltb);
        objs.add(totalb);

        return objs;
    }

    private Csbt getCsbt(LocalDate start, LocalDate stop)
    {
        CsbtRepository csbtr = repos.getCsbtRepository();

        List<Csbt> data = csbtr.findAllByDtBetweenOrderByDtAsc(start,stop);

        return data.get(0);
    }

    private Mlt getMlt(LocalDate start, LocalDate stop)
    {
        MltRepository mltr = repos.getMltRepository();

        List<Mlt> data = mltr.findAllByDtBetweenOrderByDtAsc(start,stop);

        return data.get(0);
    }

    private List<Ledger> getMlData(LocalDate start, LocalDate stop) {
        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Ledger> ret = new ArrayList<Ledger>();

        LtypeRepository ltypeRepository = repos.getLtypeRepository();
        Ltype mlrltype = ltypeRepository.findByName("MLR");
        Ltype managedltype = ltypeRepository.findByName("ManagedML");
        Ltype siraltype = ltypeRepository.findByName("StephIra");
        Ltype qiraltype = ltypeRepository.findByName("QIra");
        Ltype srothltype = ltypeRepository.findByName("StephRoth");
        Ltype qrothltype = ltypeRepository.findByName("QRoth");
        Ltype netnumltype = ltypeRepository.findByName("NetNumberIra");

        List<Ledger> mlrd = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,mlrltype);
        List<Ledger> managedd = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,managedltype);
        List<Ledger> sirad = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,siraltype);
        List<Ledger> qirad = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,qiraltype);
        List<Ledger> srothd = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,srothltype);
        List<Ledger> qrothd = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,qrothltype);
        List<Ledger> netnumd = lrepo.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start,stop,netnumltype);

        ret.add(mlrd.get(0));
        ret.add(managedd.get(0));
        ret.add(sirad.get(0));
        ret.add(qirad.get(0));
        ret.add(srothd.get(0));
        ret.add(qrothd.get(0));
        ret.add(netnumd.get(0));

        return ret;
    }
    private void pall(FileWriter w, List<Ledger> mldata, Csbt csbt, Mlt mlt,  List<nbBean> current) throws Exception {
        double mlrd = 0;
        double managedMLd = 0;
        double stephirad = 0;
        double qirad = 0;
        double stephrothd = 0;
        double qrothd = 0;
        double netnumd = 0;
        double total = 0;

        for (Ledger l : mldata) {
            total += l.getAmount();
            int sid = l.getLtype().getId();
            switch (sid) {
                case 15:
                    mlrd = l.getAmount();
                    break;
                case 16:
                    managedMLd = l.getAmount();
                    break;
                case 17:
                    stephirad = l.getAmount();
                    break;
                case 18:
                    qirad = l.getAmount();
                    break;
                case 19:
                    stephrothd = l.getAmount();
                    break;
                case 20:
                    qrothd = l.getAmount();
                    break;
                case 21:
                    netnumd = l.getAmount();
                    break;
            }
        }
        for (nbBean objs : current){
            if (objs.getLabel().equals("MLR")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - mlrd));
            }
            if (objs.getLabel().equals("ManagedML")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - managedMLd));
            }
            if (objs.getLabel().equals("StephIra")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - stephirad));
            }
            if (objs.getLabel().equals("QIra")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - qirad));
            }
            if (objs.getLabel().equals("StephRoth")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - stephrothd));
            }
            if (objs.getLabel().equals("QRoth")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() - qrothd));
            }
            if (objs.getLabel().equals("WorkIra")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() -  netnumd));
            }
            if (objs.getLabel().equals("CSB")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() -  csbt.getBalance()));
            }
            if (objs.getLabel().equals("MLCash")) {
                objs.setChange(Utils.convertDouble(objs.getCurrent() -  mlt.getBalance()));
            }
            if (objs.getLabel().equals("Total")) {
                total += csbt.getBalance();
                total += mlt.getBalance();
                total = Utils.convertDouble(total);
                objs.setChange(Utils.convertDouble(objs.getCurrent() -  total));
            }
        }

        List<String> order = new ArrayList<String>();
        order.add("Label");
        order.add("Current");
        order.add("Change");
        GLObj gobj = new GLObj(order);

        for (nbBean nobj : current) {
            gobj.add(nobj);
        }

        gobj.Print(w);
    }
}
