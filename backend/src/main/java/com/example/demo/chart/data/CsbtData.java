package com.example.demo.chart.data;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Csbt;
import com.example.demo.repository.CsbtRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CsbtData extends BaseData<Csbt> {
    private final CsbtRepository repository;

    public CsbtData(CsbtRepository l)
    {
        this.repository = l;
    }

    public List<Csbt> getData(StartStop dates) {
        return repository.findAllByDtBetweenOrderByDtAsc(dates.getStart(), dates.getStop());
    }

    public List<Csbt> monthFilter(List<Csbt> data, int month) {
        return data.stream().filter(it -> (it.getDt().getMonth().getValue() == month)).collect(Collectors.toList());
    }
}
