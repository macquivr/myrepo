package com.example.demo.utils.idate;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Utilities;
import com.example.demo.utils.dvi.BVdvi;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.dvi.Udvi;

import java.time.LocalDate;

public class BVIDate implements Idate {
    private Budget data;

    public BVIDate(Budget u) { data = u; }
    public LocalDate getDate() { return data.getBdate(); }
    public Dvi getData() { return new BVdvi(this.data); }
}