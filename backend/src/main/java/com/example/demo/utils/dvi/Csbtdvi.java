package com.example.demo.utils.dvi;

import com.example.demo.domain.Csbt;

import java.time.LocalDate;

public class Csbtdvi implements Dvi {
    private final Csbt data;

    public Dvi factory(Object obj)
    {
        return new Csbtdvi(obj);
    }

    public Csbtdvi(Object l) {
        this.data = (Csbt) l;
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
