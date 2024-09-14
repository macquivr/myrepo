package com.example.demo.chart.dvi;

import com.example.demo.domain.Mlt;
import com.example.demo.utils.dvi.Dvi;

import java.time.LocalDate;

public class Mltdvi implements Dvi {
    private final Mlt data;

    public Dvi factory(Object obj)
    {
        return new Mltdvi(obj);
    }

    public Mltdvi(Object l) {
        this.data = (Mlt) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getDt();
    }

    public double aValue() {
        return this.data.getBalance();
    }
}
