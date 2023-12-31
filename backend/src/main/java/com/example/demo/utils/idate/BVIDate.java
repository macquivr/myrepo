package com.example.demo.utils.idate;

import com.example.demo.domain.Budget;
import com.example.demo.utils.dvi.BVdvi;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class BVIDate implements Idate {
    private final Budget data;

    public BVIDate(Budget u) {
        this.data = u;
    }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BVdvi(this.data); }
}
