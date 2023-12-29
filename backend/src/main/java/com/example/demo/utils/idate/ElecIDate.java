package com.example.demo.utils.idate;

import com.example.demo.domain.Utilities;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.dvi.Elecdvi;

import java.time.LocalDate;

public class ElecIDate implements Idate {
    private Utilities data;

    public ElecIDate(Utilities u) { data = u; }
    public LocalDate getDate() { return data.getDate(); }
    public Dvi getData() { return new Elecdvi(this.data); }
}
