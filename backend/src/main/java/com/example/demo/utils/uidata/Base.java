package com.example.demo.utils.uidata;

import com.example.demo.bean.StartStop;
import com.example.demo.dto.SessionDTO;
import com.example.demo.state.Consolidate;
import com.example.demo.domain.Ledger;
import com.example.demo.utils.mydate.DUtil;
import com.example.demo.utils.mydate.DateSorter;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.Vector;

public abstract class Base {

    public void go(SessionDTO session, StartStop dates, List<Ledger> ldata, List lst)
    {
        if ((ldata == null) || (ldata.size() == 0)) {
            System.out.println("NO DATA!");
            return;
        }

        List<Idate> d = new Vector<Idate>();
        for (Ledger l : ldata)
            d.add(new LedgerIDate(l));
        go(session,dates,d,lst,true);
    }
    public void go(SessionDTO session, StartStop dates, List<Idate> ldata, List lst, boolean lg)
    {
        if ((ldata == null) || (ldata.size() == 0))
            return;
        LocalDate old = null;
        LocalDate nxt = null;
        if (session.getConsolidate() != Consolidate.NONE) {
            old = dates.getStart();
            nxt = calculateNext(dates.getStart(), session.getConsolidate());
        }

        Object obj = factory();
        Object total = factory();

        Collections.sort(ldata, new DateSorter());

        for (Idate l : ldata) {
            if ((nxt != null) && DUtil.isEqualToOrAfter(l.getDate(),nxt)) {
                addStuff(lst, obj, DUtil.getDate(old, DUtil.MMMYYYY));
                obj = factory();
                old = nxt;
                nxt = calculateNext(nxt, session.getConsolidate());
            }
            apply(l,obj);
            apply(l,total);
        }
        if (session.getConsolidate() != Consolidate.NONE)
            addStuff(lst, obj, DUtil.getDate(old, DUtil.MMMYYYY));
        addStuff(lst,total,"Total");
    }

    public abstract Object factory();
    public abstract void addStuff(List l, Object data, String dstr);
    public abstract void apply(Idate l, Object obj);

    private LocalDate calculateNext(LocalDate cur, Consolidate type) {
        if (type == Consolidate.MONTHLY) {
            return cur.plusMonths(1);
        }
        if (type == Consolidate.QUARTERLY) {
            return cur.plusMonths(3);
        }
        if (type == Consolidate.HALF) {
            return cur.plusMonths(6);
        }
        if (type == Consolidate.YEARLY) {
            return cur.plusMonths(12);
        }
        return null;
    }
}
