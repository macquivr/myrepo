package com.example.demo.utils.idate;

import com.example.demo.domain.Budgets;
import com.example.demo.utils.dvi.BNSdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class BNSIDate implements Idate {
    private final Budgets data;

    public BNSIDate(Budgets u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BNSdvi(this.data); }
}
