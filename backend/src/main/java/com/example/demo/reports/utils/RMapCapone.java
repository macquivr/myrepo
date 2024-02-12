package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapCapone implements RMapI {
    private int label = 0;

    public RMapCapone() {
        this.label = 10264;
    }

    public RMapCapone(int l) {
        this.label = l;
    }
    public boolean apply(Ledger data) {
        return (data.getLabel().getId() == this.label);
    }
}
