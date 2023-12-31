package com.example.demo.utils.idate;

import com.example.demo.domain.Utilities;
import com.example.demo.utils.dvi.Cabledvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class CableIDate implements Idate {
    private final Utilities data;

    public CableIDate(Utilities u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getDate(); }
    public Dvi getData() { return new Cabledvi(this.data); }
}
