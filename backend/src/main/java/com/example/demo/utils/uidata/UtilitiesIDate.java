package com.example.demo.utils.uidata;

import com.example.demo.domain.Utilities;
import java.time.LocalDate;

public class UtilitiesIDate implements Idate {
    private Utilities data;

    public UtilitiesIDate(Utilities u) { data = u; }
    public LocalDate getDate() { return data.getDate(); }
    public Object getData() { return data; }
}
