package com.example.demo.utils.dvi;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgets;

import java.time.LocalDate;

public class BNSdvi implements Dvi {
    private Budgets data;

    public Dvi factory(Object obj)
    {
        return new BNSdvi(obj);
    }

    public BNSdvi(Object l) {
        this.data = (Budgets) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getBdate();
    }

    public double aValue() {
        return this.data.getNet();
    }
}
