package com.example.demo.utils;

import com.example.demo.domain.Ledger;
import com.example.demo.domain.Ltype;
import com.example.demo.domain.Stype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.services.LedgerService;
import com.example.demo.state.WhichDate;
import com.example.demo.repository.LedgerRepository;
import com.example.demo.bean.StartStop;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class LData {
    private static final Logger logger= LoggerFactory.getLogger(LedgerService.class);
    private LedgerRepository repository;
    private StartStop dates = null;

    public LData(LedgerRepository l)
    {
        repository = l;
        dates = new StartStop();
    }

    public StartStop getDates() { return dates; }
    public List<Ledger> filterByDate(SessionDTO filter, Stype stype, Ltype ltype)
    {
        WhichDate w = filter.getWhichDate();

        if (w.equals(WhichDate.NONE)) {
            LocalDate start = DUtil.first();
            LocalDate stop = DUtil.stopNow();
            return doFilter(start,stop,null,null);
        }

        if (w.equals(WhichDate.BOTH)) {
            return doFilter(filter.getStart(),filter.getStop(),stype,ltype);
        }

        if (w.equals(WhichDate.START))
            return findSpecific(filter.getStart(),stype,ltype);

        if (w.equals(WhichDate.MONTH))
            return findByMonth(filter.getStart(),stype,ltype);

        if (w.equals(WhichDate.YEAR))
            return findByYear(filter.getStart(),stype,ltype);

        if (w.equals(WhichDate.Q1))
            return findByQH(filter.getStart(),1,2,stype,ltype);

        if (w.equals(WhichDate.Q2))
            return findByQH(filter.getStart(),4,2,stype,ltype);

        if (w.equals(WhichDate.Q3))
            return findByQH(filter.getStart(),7,2,stype,ltype);

        if (w.equals(WhichDate.Q4))
            return findByQH(filter.getStart(),10,2,stype,ltype);

        if (w.equals(WhichDate.FH)) {
            return findByQH(filter.getStart(), 1,5,stype,ltype);
        }

        if (w.equals(WhichDate.SH)) {
            return findByQH(filter.getStart(), 7,5,stype,ltype);
        }

        logger.error("Bad Type " + w);
        return new Vector<Ledger>();
    }

    public void filterLow(List<Ledger> df) {
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : df) {
            int lid = l.getLtype().getId();
            if ((lid > 14) || (lid == 4))
                death.add(l);
        }
        df.removeAll(death);
    }

    public void filterBundle(List<Ledger> df) {
        List<Ledger> death = new Vector<Ledger>();
        for (Ledger l : df) {
            int lid = l.getLtype().getId();
            if ((lid != 3) &&
                    (lid != 5) &&
                    (lid != 6) &&
                    (lid != 11) &&
                    (lid != 12) &&
                    (lid != 14))
                death.add(l);
        }
        df.removeAll(death);
    }

    private List<Ledger> doFilter(LocalDate start, LocalDate stop, Stype stype, Ltype ltype) {
        dates.setStart(start);
        dates.setStop(stop);
       
        if ((stype == null) && (ltype == null)) {
           return repository.findAllByTransdateBetweenOrderByTransdateAsc(start, stop);
        }
        if ((stype != null) && (ltype != null)) {
            return repository.findAllByTransdateBetweenAndLtypeAndStypeOrderByTransdateAsc(start, stop, ltype, stype);
        }
        if (ltype != null) {
            return repository.findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(start, stop, ltype);
        }

        return repository.findAllByTransdateBetweenAndStypeOrderByTransdateAsc(start, stop, stype);
    }
    private List<Ledger> findByQH(LocalDate d,int m,int inc, Stype stype, Ltype ltype) {
        LocalDate stop = DUtil.setMonth(d,m).plusMonths(inc);

        return doFilter(d,stop, stype, ltype);
    }

    private List<Ledger> findSpecific(LocalDate d, Stype stype, Ltype ltype) {
        LocalDate start = DUtil.firstOfMonth(d);
        LocalDate stop = DUtil.lastOfMonth(d);

        return doFilter(start,stop,stype,ltype);
    }

    private List<Ledger> findByYear(LocalDate d, Stype stype,Ltype ltype) {
        LocalDate start = DUtil.firstOfYear(d);
        LocalDate stop = DUtil.lastOfYear(d);

        return doFilter(start,stop,stype,ltype);
    }

    private List<Ledger> findByMonth(LocalDate d, Stype stype, Ltype ltype) {
        int month = d.getMonth().getValue();
        LocalDate start = DUtil.first();
        LocalDate stop = DUtil.stopNow();
        List<Ledger> data = doFilter(start,stop,stype,ltype);

        return data.stream().filter(it -> (it.getTransdate().getMonth().getValue() == month)).collect(Collectors.toList());
    }
}
