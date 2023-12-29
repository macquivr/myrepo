package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapEmma implements RMapI {

    public boolean apply(Ledger data) {
        return (data.getStype().getId() == 14);
    }
}
