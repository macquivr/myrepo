package com.example.demo.utils;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Csbt;
import com.example.demo.domain.Utilities;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.CsbtRepository;
import com.example.demo.repository.UtilitiesRepository;
import com.example.demo.services.CsbtService;
import com.example.demo.state.WhichDate;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class CsbtData {
    private static final Logger logger= LoggerFactory.getLogger(CsbtService.class);
    private final CsbtRepository repository;
    private final StartStop dates;

    public CsbtData(CsbtRepository l)
    {
        this.repository = l;
        this.dates = new StartStop();
    }

    public StartStop getDates() { return dates; }
    public List<Csbt> filterByDate(SessionDTO filter)
    {
        WhichDate w = filter.getWhichDate();

        if (w.equals(WhichDate.NONE)) {
            LocalDate start = DUtil.first();
            LocalDate stop = DUtil.stopNow();
            return doFilter(start,stop);
        }

        if (w.equals(WhichDate.BOTH)) {
            return doFilter(filter.getStart(),filter.getStop());
        }

        if (w.equals(WhichDate.START))
            return findSpecific(filter.getStart());

        if (w.equals(WhichDate.MONTH))
            return findByMonth(filter.getStart());

        if (w.equals(WhichDate.YEAR))
            return findByYear(filter.getStart());

        if (w.equals(WhichDate.Q1))
            return findByQH(filter.getStart(),1,2);

        if (w.equals(WhichDate.Q2))
            return findByQH(filter.getStart(),4,2);

        if (w.equals(WhichDate.Q3))
            return findByQH(filter.getStart(),7,2);

        if (w.equals(WhichDate.Q4))
            return findByQH(filter.getStart(),10,2);

        if (w.equals(WhichDate.FH)) {
            return findByQH(filter.getStart(), 1,5);
        }

        if (w.equals(WhichDate.SH)) {
            return findByQH(filter.getStart(), 7,5);
        }

        logger.error("Bad Type " + w);
        return new Vector<>();
    }

    private List<Csbt> doFilter(LocalDate start, LocalDate stop) {
        dates.setStart(start);
        dates.setStop(stop);
       
        return repository.findAllByDtBetweenOrderByDtAsc(start, stop);
    }
    private List<Csbt> findByQH(LocalDate d,int m,int inc) {
        LocalDate stop = DUtil.setMonth(d,m).plusMonths(inc);

        return doFilter(d,stop);
    }

    private List<Csbt> findSpecific(LocalDate d) {
        LocalDate start = DUtil.firstOfMonth(d);
        LocalDate stop = DUtil.lastOfMonth(d);

        return doFilter(start,stop);
    }

    private List<Csbt> findByYear(LocalDate d) {
        LocalDate start = DUtil.firstOfYear(d);
        LocalDate stop = DUtil.lastOfYear(d);

        return doFilter(start,stop);
    }

    private List<Csbt> findByMonth(LocalDate d) {
        int month = d.getMonth().getValue();
        LocalDate start = DUtil.first();
        LocalDate stop = DUtil.stopNow();
        List<Csbt> data = doFilter(start,stop);

        return data.stream().filter(it -> (it.getDt().getMonth().getValue() == month)).collect(Collectors.toList());
    }
}
