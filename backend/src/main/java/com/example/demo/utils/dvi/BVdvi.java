package com.example.demo.utils.dvi;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Utilities;
import com.example.demo.utils.Utils;

import java.time.LocalDate;

public class BVdvi implements Dvi {
    private Budget data;

    public Dvi factory(Object obj)
    {
        return new BVdvi(obj);
    }

    public BVdvi(Object l) {
        this.data = (Budget) l;
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
