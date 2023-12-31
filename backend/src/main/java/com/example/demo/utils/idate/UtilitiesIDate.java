package com.example.demo.utils.idate;

import com.example.demo.domain.Utilities;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.dvi.Udvi;

import java.time.LocalDate;

public class UtilitiesIDate implements Idate {
    private final Utilities data;

    public UtilitiesIDate(Utilities u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDate(); }
    public Dvi getData() { return new Udvi(this.data); }
}
