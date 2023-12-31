package com.example.demo.utils.idate;

import com.example.demo.domain.Utilities;
import com.example.demo.utils.dvi.Celldvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class CellIDate implements Idate {
    private final Utilities data;

    public CellIDate(Utilities u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDate(); }
    public Dvi getData() { return new Celldvi(this.data); }
}
