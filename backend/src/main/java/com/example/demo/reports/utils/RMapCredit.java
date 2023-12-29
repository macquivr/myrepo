package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapCredit implements RMapI {

    public boolean apply(Ledger data) {
        return ((data.getLabel().getId() == 11209) ||
                (data.getLabel().getId() == 10264) ||
                (data.getLabel().getId() == 10019) ||
                (data.getLabel().getId() == 12933));
    }
}
