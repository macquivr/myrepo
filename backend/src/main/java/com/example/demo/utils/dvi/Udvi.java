package com.example.demo.utils.dvi;

import com.example.demo.domain.Utilities;
import com.example.demo.utils.Utils;

import java.time.LocalDate;

public class Udvi implements Dvi {
    private final Utilities data;

    public Dvi factory(Object obj)
    {
        return new Udvi(obj);
    }

    public Udvi(Object l) {
        this.data = (Utilities) l;
    }

    public Object getObj() {
        return this.data;
    }
    public LocalDate dValue() {
        return this.data.getDate();
    }

    public double aValue() {
        double cable = this.data.getCable();
        double cell = this.data.getCell();
        double electric = this.data.getElectric();

        return Utils.convertDouble(cable + cell + electric);
    }
}
