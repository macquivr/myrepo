package com.example.demo.utils;

import com.example.demo.domain.TLedger;
import com.example.demo.domain.Ltype;
import com.example.demo.dto.SessionDTO;
import com.example.demo.services.LedgerService;
import com.example.demo.state.WhichDate;
import com.example.demo.repository.TLedgerRepository;
import com.example.demo.bean.StartStop;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class TLData {
    private static final Logger logger= LoggerFactory.getLogger(TLData.class);
    private final TLedgerRepository repository;
    private final StartStop dates;

    public TLData(TLedgerRepository l)
    {
        this.repository = l;
        this.dates = new StartStop();
    }

    public StartStop getDates() { return dates; }
    public List<TLedger> filterByDate(SessionDTO filter, Ltype ltype)
    {
        WhichDate w = filter.getWhichDate();

        if (w.equals(WhichDate.NONE)) {
            LocalDate start = DUtil.first();
            LocalDate stop = DUtil.stopNow();
            return doFilter(start,stop,null);
        }

        if (w.equals(WhichDate.BOTH)) {
            return doFilter(filter.getStart(),filter.getStop(),ltype);
        }

        if (w.equals(WhichDate.START))
            return findSpecific(filter.getStart(),ltype);

        if (w.equals(WhichDate.MONTH))
            return findByMonth(filter.getStart(),ltype);

        if (w.equals(WhichDate.YEAR))
            return findByYear(filter.getStart(),ltype);

        if (w.equals(WhichDate.Q1))
            return findByQH(filter.getStart(),1,2,ltype);

        if (w.equals(WhichDate.Q2))
            return findByQH(filter.getStart(),4,2,ltype);

        if (w.equals(WhichDate.Q3))
            return findByQH(filter.getStart(),7,2,ltype);

        if (w.equals(WhichDate.Q4))
            return findByQH(filter.getStart(),10,2,ltype);

        if (w.equals(WhichDate.FH)) {
            return findByQH(filter.getStart(), 1,5,ltype);
        }

        if (w.equals(WhichDate.SH)) {
            return findByQH(filter.getStart(), 7,5,ltype);
        }

        logger.error("Bad Type " + w);
        return new Vector<>();
    }

    public void filterLow(List<TLedger> df) {
        List<TLedger> death = new Vector<>();
        for (TLedger l : df) {
            int lid = l.getLtype().getId();
            if ((lid > 14) || (lid == 4))
                death.add(l);
        }
        df.removeAll(death);
    }

    public void filterBundle(List<TLedger> df) {
        List<TLedger> death = new Vector<>();
        for (TLedger l : df) {
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

    private List<TLedger> doFilter(LocalDate start, LocalDate stop, Ltype ltype) {
        dates.setStart(start);
        dates.setStop(stop);
       
        if (ltype == null) {
           return repository.findAllByTdateBetweenOrderByTdateAsc(start, stop);
        } 
        return repository.findAllByTdateBetweenAndLtypeOrderByTdateAsc(start, stop, ltype);
    }

    private List<TLedger> findByQH(LocalDate d,int m,int inc, Ltype ltype) {
        LocalDate stop = DUtil.setMonth(d,m).plusMonths(inc);

        return doFilter(d,stop, ltype);
    }

    private List<TLedger> findSpecific(LocalDate d, Ltype ltype) {
        LocalDate start = DUtil.firstOfMonth(d);
        LocalDate stop = DUtil.lastOfMonth(d);

        return doFilter(start,stop,ltype);
    }

    private List<TLedger> findByYear(LocalDate d, Ltype ltype) {
        LocalDate start = DUtil.firstOfYear(d);
        LocalDate stop = DUtil.lastOfYear(d);

        return doFilter(start,stop,ltype);
    }

    private List<TLedger> findByMonth(LocalDate d, Ltype ltype) {
        int month = d.getMonth().getValue();
        LocalDate start = DUtil.first();
        LocalDate stop = DUtil.stopNow();
        List<TLedger> data = doFilter(start,stop,ltype);

        return data.stream().filter(it -> (it.getTdate().getMonth().getValue() == month)).collect(Collectors.toList());
    }
}
