package com.example.demo.utils.dvi;

import com.example.demo.domain.Ledger;
import java.time.LocalDate;

public class Ldvi implements Dvi {
    private final Ledger data;

    public Dvi factory(Object obj)
    {
        return new Ldvi(obj);
    }

    public Ldvi(Object l) {
        this.data = (Ledger) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getTransdate();
    }

    public double aValue() {
        return this.data.getAmount();
    }
}
