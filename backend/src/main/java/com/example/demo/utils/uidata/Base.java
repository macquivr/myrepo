package com.example.demo.utils.uidata;

import com.example.demo.bean.StartStop;
import com.example.demo.dto.SessionDTO;
import com.example.demo.state.Consolidate;
import com.example.demo.utils.ConsolidateUtils;
import com.example.demo.utils.idata.baseIData;
import com.example.demo.utils.idate.Idate;
import com.example.demo.utils.mydate.DUtil;
import com.example.demo.utils.mydate.DateSorter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.time.LocalDate;
import java.util.Vector;

public abstract class Base {

    public void go(SessionDTO session, StartStop dates, baseIData ldata, List lst)
    {
        if ((ldata == null) || (ldata.getData().isEmpty())) {
            System.out.println("NO DATA!");
            return;
        }

        List<Idate> d = populateIdate(session, ldata,dates);

        go(session,dates,d,lst);
    }
    private List<Idate> populateIdate(SessionDTO session, baseIData ldata, StartStop dates) {
        List<Idate> ret = new Vector<Idate>();

        List<Idate> d = new Vector<Idate>();
        List lst = ldata.getData();

        for (Object obj : lst)
            d.add(ldata.factory(obj));
        Collections.sort(d, new DateSorter());

        Iterator<Idate> iter = d.iterator();
        Idate cur = iter.next();

        LocalDate old = null;
        LocalDate nxt = null;
        if (session.getConsolidate() != Consolidate.NONE) {
            old = dates.getStart();
            nxt = calculateNext(dates.getStart(), session.getConsolidate());
        } else {
            return d;
        }

        boolean done = false;
        boolean found = false;
        while (!done) {
            if (cur != null) {
                if ((nxt != null) && cur.getDate().isBefore(nxt)) {
                    found = true;
                    ret.add(ldata.factory(cur.getData().getObj()));
                    if (iter.hasNext())
                        cur = iter.next();
                    else
                        cur = null;
                } else {
                    if (!found) {
                        ret.add(ldata.factory(old));
                    }
                    found = false;
                    old = nxt;
                    nxt = calculateNext(nxt, session.getConsolidate());
                }
            } else {
                nxt = calculateNext(nxt, session.getConsolidate());
            }
            done = (nxt == null) || DUtil.isEqualToOrAfter(nxt, dates.getStop());
        }

        return ret;
    }
    public void go(SessionDTO session, StartStop dates, List<Idate> ldata, List lst)
    {
        if ((ldata == null) || (ldata.isEmpty()))
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
                addStuff(lst, obj, DUtil.getDate(session.getConsolidate(), old, DUtil.MMMYYYY));
                obj = factory();
                old = nxt;
                nxt = calculateNext(nxt, session.getConsolidate());
            }
            apply(l,obj);
            apply(l,total);
        }
        if (session.getConsolidate() != Consolidate.NONE)
            addStuff(lst, obj, DUtil.getDate(session.getConsolidate(), old, DUtil.MMMYYYY));
        addStuff(lst,total,"Total");
    }

    public abstract Object factory();
    public abstract void addStuff(List l, Object data, String dstr);
    public abstract void apply(Idate l, Object obj);

    private LocalDate calculateNext(LocalDate cur, Consolidate type) {
        if (type == Consolidate.MONTHLY)  {
            return cur.plusMonths(1);
        }
        if (type == Consolidate.QUARTERLY) {
            return cur.plusMonths(3);
        }
        if (type == Consolidate.HALF) {
            return cur.plusMonths(6);
        }
        if (ConsolidateUtils.isYearly(type)) {
            return cur.plusMonths(12);
        }

        return null;
    }
}
