package com.example.demo.state;

import java.util.List;

import com.example.demo.utils.RowDTOI;

public class TableDTOBase {

    protected void percent(List data)
    {
        for (Object o : data) {
            RowDTOI d = (RowDTOI) o;
            d.makePercent();
        }
    }
}
