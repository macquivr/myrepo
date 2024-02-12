package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapAmazon implements RMapI {
    private int label = 0;

    public RMapAmazon() {
        this.label = 10019;
    }

    public RMapAmazon(int l) {
        this.label = l;
    }
    public boolean apply(Ledger data) {
        return (data.getLabel().getId() == this.label);
    }
}
