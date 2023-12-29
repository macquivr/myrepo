package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapIn implements RMapI {

    public boolean apply(Ledger data) {
        return ((data.getAmount() > 0) && (data.getStype().getId() != 8));
    }
}
