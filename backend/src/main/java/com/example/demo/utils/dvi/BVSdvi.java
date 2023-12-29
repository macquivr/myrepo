package com.example.demo.utils.dvi;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgets;

import java.time.LocalDate;

public class BVSdvi implements Dvi {
    private Budgets data;

    public Dvi factory(Object obj)
    {
        return new BVSdvi(obj);
    }

    public BVSdvi(Object l) {
        this.data = (Budgets) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getBdate();
    }

    public double aValue() {
        return this.data.getValue();
    }
}
