package com.example.demo.utils.idate;

import com.example.demo.domain.Budget;
import com.example.demo.utils.dvi.BNdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class BNIDate implements Idate {
    private final Budget data;

    public BNIDate(Budget u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BNdvi(this.data); }
}
