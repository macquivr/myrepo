package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapPos implements RMapI {

    public boolean apply(Ledger data) {
        return (data.getStype().getId() == 3);
    }
}
