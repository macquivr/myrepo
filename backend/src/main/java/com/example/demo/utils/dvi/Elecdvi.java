package com.example.demo.utils.dvi;

import com.example.demo.domain.Utilities;

import java.time.LocalDate;

public class Elecdvi implements Dvi {
    private final Utilities data;

    public Dvi factory(Object obj)
    {
        return new Elecdvi(obj);
    }

    public Elecdvi(Object l) {
        this.data = (Utilities) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getDate();
    }

    public double aValue() {
        return this.data.getElectric();
    }
}
