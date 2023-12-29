package com.example.demo.utils.dvi;

import java.time.LocalDate;

public interface Dvi {
    Dvi factory(Object obj);
    Object getObj();
    LocalDate dValue();
    double aValue();
}
