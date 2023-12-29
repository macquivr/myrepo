package com.example.demo.utils.idate;

import com.example.demo.domain.Budget;
import com.example.demo.utils.dvi.BNdvi;
import com.example.demo.utils.dvi.BVdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class BNIDate implements Idate {
    private Budget data;

    public BNIDate(Budget u) { data = u; }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BNdvi(this.data); }
}
