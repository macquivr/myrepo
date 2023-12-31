package com.example.demo.reports;

import com.example.demo.bean.MRBean;
import com.example.demo.bean.MRBeanl;
import com.example.demo.bean.StartStop;
import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.ui.BVNRowDTO;
import com.example.demo.dto.ui.BVNTableDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.BudgetValuesRepository;
import com.example.demo.repository.CategoryRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class BReport implements ReportI {
    private final Repos repos;

    public BReport(Repos r) {
        repos = r;
    }
    

    public void go(FileWriter w, SessionDTO session) throws Exception {
        LData ld = new LData(repos.getLedgerRepository());
        List<Ledger> data = ld.filterByDate(session, null, null);
        ld.filterBundle(data);
        StartStop dates = ld.getDates();
        noTransfer(data);
        HashMap<String, Integer> map = getMap();
        HashMap<String, Double> vmap = getMapV();
        BVNTableDTO values = doBvalues(session, map);
        BVNTableDTO svalues = doBvs(session, map);
        BVNTableDTO net = doBnet(session, map);
        BVNTableDTO snet = doBsnet(session, map);

        List<BVNRowDTO> rlvalues = values.getBvn();
        List<BVNRowDTO> rlsvalues = svalues.getBvn();
        List<BVNRowDTO> rlnet = net.getBvn();
        List<BVNRowDTO> rlsnet = snet.getBvn();

        System.out.println("RLV: " + rlvalues.size());
        if (rlvalues.size() != 2) {
            w.write("Bad rvalue count.\n");
            return;
        }

        if (rlsvalues.size() != 2) {
            w.write("Bad rsvalue count.\n");
            return;
        }

        if (rlnet.size() != 2) {
            w.write("Bad rnet count.\n");
            return;
        }

        if (rlsnet.size() != 2) {
            w.write("Bad rsnet count.\n");
            return;
        }

        Consolidate c = session.getConsolidate();
        int m = 1;
        if (c == Consolidate.QUARTERLY) {
            m = 3;
        }
        if (c == Consolidate.HALF) {
            m = 6;
        }
        if (c == Consolidate.YEARLY) {
            m = 12;
        }

        printPeriod(w, dates);
        w.write("\n");

        pIn(w,data);

        MRBean pos = validatePos(w, m, vmap.get("Pos"), rlvalues.get(0).getPos(), rlsvalues.get(0).getPos(), rlnet.get(0).getPos(), rlsnet.get(0).getPos(), data);
        if (pos == null)
            return;

        MRBean atm = validateAtm(w, m, vmap.get("Atm"), rlvalues.get(0).getAtm(), rlsvalues.get(0).getAtm(), rlnet.get(0).getAtm(), rlsnet.get(0).getAtm(), data);
        if (atm == null)
            return;

        MRBean utils = validateStuff(w, 10344, m, "Utils", vmap, rlvalues.get(0).getUtils(), rlnet.get(0).getUtils(), data);
        if (utils == null)
            return;

        MRBean usaa = validateStuff(w, 11209, m, "Usaa", vmap, rlvalues.get(0).getUsaa(), rlnet.get(0).getUsaa(), data);
        if (usaa == null)
            return;

        MRBean capone = validateStuff(w, 10264, m, "Capone", vmap, rlvalues.get(0).getCapone(), rlnet.get(0).getCapone(), data);
        if (capone == null)
            return;

        MRBean aaa = validateStuff(w, 12933, m, "Aaa", vmap, rlvalues.get(0).getAaa(), rlnet.get(0).getAaa(), data);
        if (aaa == null)
            return;

        MRBean amazon = validateStuff(w, 10019, m, "Amazon", vmap, rlvalues.get(0).getAmazon(), rlnet.get(0).getAmazon(), data);
        if (amazon == null)
            return;

        MRBean emma = validateStuff(w, 10612, m, "Emma", vmap, rlvalues.get(0).getEmma(), rlnet.get(0).getEmma(), data);
        if (emma == null)
            return;

        MRBean dog = validateDog(w, m, vmap, rlvalues.get(0).getDog(), rlnet.get(0).getDog(), data);
        if (dog == null)
            return;



        MRBeanl budgetl = new MRBeanl();
        budgetl.add(utils);
        budgetl.add(usaa);
        budgetl.add(capone);
        budgetl.add(aaa);
        budgetl.add(amazon);
        budgetl.add(pos);
        budgetl.add(atm);
        budgetl.add(emma);
        budgetl.add(dog);
        budgetl.Print(w);

        w.write("\n");
        double other = utils.getOther();
        other += usaa.getOther();
        other += capone.getOther();
        other += aaa.getOther();
        other += amazon.getOther();
        if (other > 0) {
            w.write("Non Main Credit: " + other + "\n");
        }

        pbills(w, data);

        potherStype(w,data,"Credit");
        potherStype(w,data,"Annual");
        w.write("\n");

        consolidateMisc(data);
        printMisc(w, data);

        prest(w,data);
        w.write("\n");

        pSpent(w,vmap, m, rlsvalues.get(0),rlsnet.get(0));
    }

    private double mult(HashMap<String, Double> vmap, String tag, int m)
    {
        Double v = vmap.get(tag);
        if (v == null) {
            return 0;
        }
        return v * m;
    }
    private void pSpent(FileWriter w, HashMap<String, Double> vmap,int m, BVNRowDTO svalues, BVNRowDTO snet) {
        MRBeanl budgetl = new MRBeanl();

        double utilsValue = mult(vmap, "Utils", m);
        MRBean utils = new MRBean("Utils", svalues.getUtils(), utilsValue, snet.getUtils());
        budgetl.add(utils);

        double usaaValue = mult(vmap,"Usaa", m);
        MRBean usaa = new MRBean("Usaa", svalues.getUsaa(), usaaValue, snet.getUsaa());
        budgetl.add(usaa);

        double caponeValue = mult(vmap,"Capone", m);
        MRBean capone = new MRBean("Capone", svalues.getCapone(), caponeValue, snet.getCapone());
        budgetl.add(capone);

        double aaaValue = mult(vmap,"Aaa", m);
        MRBean aaa = new MRBean("Aaa", svalues.getAaa(), aaaValue, snet.getAaa());
        budgetl.add(aaa);

        double amazonValue = mult(vmap,"Amazon", m);
        MRBean amazon = new MRBean("Amazon", svalues.getAmazon(), amazonValue, snet.getAmazon());
        budgetl.add(amazon);

        budgetl.Print(w);
    }
    private void pIn(FileWriter w, List<Ledger> data) throws Exception {
        List<Ledger> death = new Vector<>();
        double work = 0;
        double other = 0;
        double tin;
        double out = 0;
        double net;
        for (Ledger l : data) {
            if (l.getAmount() < 0) {
                out += l.getAmount();
            } else {
                death.add(l);
                if (l.getLabel().getId() == 12448)
                    work += l.getAmount();
                else {
                    other += l.getAmount();
                }
            }
        }
        data.removeAll(death);
        net = Utils.convertDouble(work + other + out);
        tin = Utils.convertDouble(work + other);

        w.write("In Work: " + Utils.convertDouble(work) + "\n");
        if (other > 0) {
            w.write("In Other: " + Utils.convertDouble(other) + "\n");
        }
        w.write("In: " + tin + "\n");
        w.write("Out: " + Utils.convertDouble(out) + "\n");
        w.write("Net: " + net + "\n");
        w.write("\n");
    }
    private void prest(FileWriter w, List<Ledger> data) throws Exception {
        boolean r = false;
        for (Ledger l : data) {
             r = true;
             w.write(l.getLabel().getName() + " " + l.getAmount() + " " + l.getStype().getName() + "\n");
        }
        if (!r) {
            w.write("\n***\n");
        }
    }

    private void potherStype(FileWriter w, List<Ledger> data, String label) throws Exception {
        List<Ledger> death = new Vector<>();
        double amt = 0;
        int cnt=0;
        for (Ledger l : data) {
            if ((l.getAmount() < 0) && (l.getStype().getName().equals(label))){
                death.add(l);
                amt += l.getAmount();
                cnt++;
            }
        }
        if (cnt > 0) {
            w.write(label + ": " + Utils.convertDouble(amt) + "\n");
        }
        data.removeAll(death);
    }

    private void noTransfer(List<Ledger> data) {
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Transfer"))
                death.add(l);
        }
        data.removeAll(death);
    }

    private void pbills(FileWriter w,List<Ledger> data) throws Exception{
        w.write("Bills:\n");

        double total = 0;
        int cnt = 0;
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("Bills")) {
                death.add(l);
                w.write(l.getLabel().getName() + " " + l.getAmount() + "\n");
                cnt++;
                total += l.getAmount();
            }
        }
        data.removeAll(death);
        if (cnt > 1) {
            w.write("Total: " + Utils.convertDouble(total) + "\n");
        }
        w.write("\n");
    }
    private MRBean validateDog(FileWriter w, int m, HashMap<String, Double> map, double rlvalues, double rlnet, List<Ledger> data) throws Exception {
        double value = mult(map,"Dog", m);

        double p = 0;
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (isDog(l))   {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad Dog rl " + p + " " + rlvalues);
            return null;
        }

        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad Dog rlnet " + n + " " + rlnet);
            return null;
        }

        return new MRBean("Dog", p, value, n);
    }

    private MRBean validateStuff(FileWriter w, int lid, int m, String bstr, HashMap<String, Double> map, double rlvalues, double rlnet, List<Ledger> data) throws Exception {
        double value = mult(map, bstr,m);

        double other = 0;
        double p = 0;
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (l.getLabel().getId() == lid)  {
                if (l.getLtype().getId() != 3) {
                    other += l.getAmount();
                }
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad " + bstr + " rl " + p + " " + rlvalues);
            return null;
        }

        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad " + bstr + " rlnet " + p + " " + rlnet);
            return null;
        }

        MRBean ret = new MRBean(bstr, p, value, n);
        ret.setOther(other);
        return ret;
    }

    private MRBean validatePos(FileWriter w, int m, Double mapv, double rlvalues, double rlsvalues, double rlnet, double rlsnet,List<Ledger> data) throws Exception {
        double value = mapv * m;
        double p = 0;
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("POS")) {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad pos rl " + p + " " + rlvalues);
            return null;
        }

        if (p != rlsvalues) {
            w.write("Bad pos rls " + p + " " + rlsvalues);
            return null;
        }
        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad pos rlnet " + p + " " + rlnet);
            return null;
        }

        if (n != rlsnet) {
            w.write("Bad pos rlsnet " + p + " " + rlsnet);
            return null;
        }

        return new MRBean("POS",p, value, n);
    }

    private MRBean validateAtm(FileWriter w, int m, Double mapv, double rlvalues, double rlsvalues, double rlnet, double rlsnet,List<Ledger> data) throws Exception {
        double value = mapv * m;
        double p = 0;
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (l.getStype().getName().equals("ATM")) {
                p += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        p = p * (-1);
        p = Utils.convertDouble(p);

        if (p != rlvalues) {
            w.write("Bad atm rl " + p + " " + rlvalues);
            return null;
        }

        if (p != rlsvalues) {
            w.write("Bad atm rls " + p + " " + rlsvalues);
            return null;
        }
        double n = Utils.convertDouble(p - value);

        if (n != rlnet) {
            w.write("Bad atm rlnet " + p + " " + rlnet);
            return null;
        }

        if (n != rlsnet) {
            w.write("Bad atm rlsnet " + p + " " + rlsnet);
            return null;
        }

        return new MRBean("ATM",p, value ,n);
    }
    public BVNTableDTO doBnet(SessionDTO session, HashMap<String, Integer> map) {
        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        BNIData ldata = new BNIData(bndata);
        ldata.setDates(dates);
        BNUI bobj = new BNUI(map);
        bobj.go(session, ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBsnet(SessionDTO session,HashMap<String, Integer> map) {
        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bndata = bdata.filterByDate(session);
        StartStop dates = bdata.getDates();

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        BNSIData ldata = new BNSIData(bndata);
        ldata.setDates(dates);
        BNSUI bobj = new BNSUI(map);
        bobj.go(session, ldata, rdata);

        return ret;
    }

    private HashMap<String, Integer> getMap() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String,Integer> map = new HashMap<>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(),b.getId());
        }
        return map;
    }

    private HashMap<String, Double> getMapV() {
        BudgetValuesRepository bvr = repos.getBudgetValuesRepository();
        List<Budgetvalues> bl = bvr.findAll();
        HashMap<String,Double> map = new HashMap<>();
        for (Budgetvalues b : bl) {
            map.put(b.getName(),b.getValue());
        }
        return map;
    }

    public BVNTableDTO doBvalues(SessionDTO session, HashMap<String, Integer> map) {
        BData bdata = new BData(repos.getBudgetRepository());
        List<Budget> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVIData ldata = new BVIData(bvdata);
        ldata.setDates(dates);
        BVUI bobj = new BVUI(map);
        bobj.go(session,  ldata, rdata);

        return ret;
    }

    public BVNTableDTO doBvs(SessionDTO session, HashMap<String, Integer> map) {
        BSData bdata = new BSData(repos.getBudgetsRepository());
        List<Budgets> bvdata = bdata.filterByDate(session);

        BVNTableDTO ret = new BVNTableDTO();
        List<BVNRowDTO> rdata = new Vector<>();
        ret.setBvn(rdata);

        StartStop dates = bdata.getDates();

        BVSIData ldata = new BVSIData(bvdata);
        ldata.setDates(dates);
        BVSUI bobj = new BVSUI(map);
        bobj.go(session, ldata, rdata);

        return ret;
    }


    private boolean isDog(Ledger l) {
        if (l.getChecks() != null) {
            Checks c = l.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                return true;
            }
        }
        return l.getLabel().getId() == 13137;
    }
    private void printPeriod(FileWriter w,StartStop dates) throws Exception {

        LocalDate dstart = dates.getStart();
        LocalDate dstop = dates.getStop();

        w.write(dstart.toString() + " ==> " + dstop.toString() + "\n");
    }

    private void consolidateMisc(List<Ledger> data) {
        List<Ledger> death = new Vector<>();
        for (Ledger l : data) {
            if (!(l.getStype().getName().equals("Misc")))
                continue;
            for (Ledger inner : data) {
                if (!(l.getStype().getName().equals("Misc")))
                    continue;
                if (death.contains(inner))
                    continue;
                if (inner == l)
                    continue;
                if (inner.getChecks() != null) {
                    if (l.getChecks() == null)
                        continue;
                    Payee pl = l.getChecks().getPayee();
                    Payee pi = inner.getChecks().getPayee();
                    if (pl == pi) {
                        death.add(inner);
                        l.setAmount(Utils.convertDouble(l.getAmount() + inner.getAmount()));
                    }
                } else {
                    if (l.getChecks() != null)
                        continue;
                    if (l.getLabel() == inner.getLabel()) {
                        death.add(inner);
                        l.setAmount(Utils.convertDouble(l.getAmount() + inner.getAmount()));
                    }
                }
            }
        }
        data.removeAll(death);
    }

    private int category(List<Ledger> data, int category) {
        int ret = 0;

        for (Ledger l : data) {
            if (!(l.getStype().getName().equals("Misc")))
                continue;
            if (l.getLabel().getCategory().getId() == category)
                ret++;
        }
        return ret;
    }
    private void printMisc(FileWriter w, List<Ledger> data) throws Exception {
        w.write("MISC:\n");
        double total = 0;
        CategoryRepository cr = repos.getCategoryRepository();
        List<Category> crl = cr.findAll();
        List<Ledger> death = new Vector<>();
        for (Category c : crl) {
            int f = category(data, c.getId());
            double t = 0;
            if (f > 0) {
                w.write("CATEGORY: " + c.getName() + "\n");
                int cnt = 0;
                for (Ledger l : data) {
                    if (!(l.getStype().getName().equals("Misc")))
                        continue;
                    if (l.getLabel().getCategory() == c) {
                        cnt++;
                        death.add(l);
                        t += l.getAmount();
                        if (l.getChecks() != null) {
                            w.write(l.getTransdate().toString() + " " + l.getChecks().getPayee().getCheckType().getName() + " " + l.getChecks().getPayee().getName() + " " + l.getAmount() + "\n");
                        } else {
                            w.write(l.getTransdate().toString() + " " + l.getLabel().getName() + " " + l.getAmount() + "\n");
                        }
                    }
                }
                total += t;
                if (cnt > 1) {
                    w.write("Total: " + Utils.convertDouble(t) + "\n\n");
                } else {
                    w.write("\n");
                }
            }
        }
        w.write("Misc Total: " + Utils.convertDouble(total));
        data.removeAll(death);
    }


}
