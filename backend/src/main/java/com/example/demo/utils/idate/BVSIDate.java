package com.example.demo.utils.idate;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgets;
import com.example.demo.utils.dvi.BVSdvi;
import com.example.demo.utils.dvi.BVdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class BVSIDate implements Idate {
    private Budgets data;

    public BVSIDate(Budgets u) { data = u; }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BVSdvi(this.data); }
}
