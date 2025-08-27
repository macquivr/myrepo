package com.example.demo.actions;

import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statement;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.repository.LtypeRepository;
import com.example.demo.domain.Ledger;
import com.example.demo.utils.Utils;
import java.io.FileWriter;
import java.util.*;

public class CustomAction extends BaseAction implements ActionI {

    private FileWriter w;

    public CustomAction(Repos r) {
        super(r);
    }

    public boolean go(SessionDTO session) throws Exception {
        try {
            this.w = new FileWriter("out.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        List<Integer> ltypes = new ArrayList<Integer>();
        //ltypes.add(3);
        ltypes.add(5);
        ltypes.add(6);
        ltypes.add(11);
        ltypes.add(12);
        ltypes.add(14);
        for (Integer I : ltypes) {
            if (!doStuff(I.intValue())) {
                return false;
            }
        }

        return true;
    }

    private boolean  doStuff(int ltype) {
        System.out.println("LTYPE: " + ltype);
        HashMap<Integer,Statement> hmap = new HashMap<Integer, Statement>();
        HashMap<Statement, List<Ledger>> smap = new HashMap<Statement, List<Ledger>>();
        LedgerRepository lr = repos.getLedgerRepository();
        LtypeRepository ltr = repos.getLtypeRepository();

        Optional<Ltype> lto = ltr.findById(ltype);
        if (!lto.isPresent()) {
            return false;
        }
        Ltype lt = lto.get();

        List<Ledger> data = lr.findAllByLtype(lt);
        for (Ledger l : data) {
            hmap.put(l.getStatement().getId(),l.getStatement());
            List<Ledger> ll = smap.get(l.getStatement());
            if (ll == null) {
                ll = new ArrayList<Ledger>();
                ll.add(l);
                smap.put(l.getStatement(),ll);
            } else {
                ll.add(l);
            }
        }
        Set<Integer> keys = hmap.keySet();

        boolean ret = true;
        for (Integer key : keys) {
            Statement value = hmap.get(key);
            List<Ledger> llk = smap.get(value);
            double total = 0;
            for (Ledger l : llk) {
                if (l.getAmount() < 0) {
                    total += l.getAmount();
                }
            }
            total = Utils.convertDouble(total) * -1;
            double check = Utils.convertDouble(value.getOuta() + value.getFee());
            if (check != total) {
                ret = false;
                System.out.println("S: " + value.getStatements().getName() + " " + check + " " + total);
            }
        }
        return ret;
    }
    private boolean other() throws Exception {
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
        LedgerRepository l = repos.getLedgerRepository();
        boolean ok = true;
        int count = 0;
        List<Ledger> data = l.findAll();
        for (Ledger outer : data) {
            for (Ledger inner : data) {
                if (inner == outer)
                    continue;
                if (inner.match(outer)) {
                    Integer I = map.get(outer.getId());
                    if (I == null) {
                        map.put(inner.getId(),outer.getId());
                        w.write("MATCH " + inner.getId() + " " + outer.getId() + "\n");
                        count++;
                        ok = false;
                    }
                }
            }
        }
        w.write("COUNT: " + count + "\n");
        w.flush();
        w.close();
        return ok;
    }
}
