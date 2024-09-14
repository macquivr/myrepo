package com.example.demo.chart.idate;

import com.example.demo.chart.dvi.Mltdvi;
import com.example.demo.domain.Mlt;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.idate.Idate;

import java.time.LocalDate;

public class MltIDate implements Idate {
    private final Mlt data;

    public MltIDate(Mlt u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDt(); }
    public Dvi getData() { return new Mltdvi(this.data); }
}
