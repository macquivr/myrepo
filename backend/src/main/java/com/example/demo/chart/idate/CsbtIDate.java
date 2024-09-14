package com.example.demo.chart.idate;

import com.example.demo.domain.Csbt;
import com.example.demo.chart.dvi.Csbtdvi;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.idate.Idate;

import java.time.LocalDate;

public class CsbtIDate implements Idate {
    private final Csbt data;

    public CsbtIDate(Csbt u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDt(); }
    public Dvi getData() { return new Csbtdvi(this.data); }
}
