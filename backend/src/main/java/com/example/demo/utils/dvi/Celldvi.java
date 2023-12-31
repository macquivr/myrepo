package com.example.demo.utils.dvi;

import com.example.demo.domain.Utilities;

import java.time.LocalDate;

public class Celldvi implements Dvi {
    private final Utilities data;

    public Dvi factory(Object obj)
    {
        return new Celldvi(obj);
    }

    public Celldvi(Object l) {
        this.data = (Utilities) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getDate();
    }

    public double aValue() {
        return this.data.getCell();
    }
}
