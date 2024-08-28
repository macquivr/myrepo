package com.example.demo.utils.idate;

import com.example.demo.domain.Csbt;
import com.example.demo.utils.dvi.Csbtdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class CsbtIDate implements Idate {
    private final Csbt data;

    public CsbtIDate(Csbt u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDt(); }
    public Dvi getData() { return new Csbtdvi(this.data); }
}
