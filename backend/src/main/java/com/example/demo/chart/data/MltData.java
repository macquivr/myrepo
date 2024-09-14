package com.example.demo.chart.data;

import com.example.demo.bean.StartStop;
import com.example.demo.domain.Mlt;
import com.example.demo.repository.MltRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MltData extends BaseData<Mlt> {
    private final MltRepository repository;

    public MltData(MltRepository l)
    {
        this.repository = l;
    }

    public List<Mlt> getData(StartStop dates) {
        return repository.findAllByDtBetweenOrderByDtAsc(dates.getStart(), dates.getStop());
    }

    public List<Mlt> monthFilter(List<Mlt> data, int month) {
        return data.stream().filter(it -> (it.getDt().getMonth().getValue() == month)).collect(Collectors.toList());
    }
}
