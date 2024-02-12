package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapUsaa implements RMapI {
    private int label = 0;

    public RMapUsaa() {
        this.label = 11209;
    }

    public RMapUsaa(int l) {
        this.label = l;
    }
    public boolean apply(Ledger data) {
        return (data.getLabel().getId() == this.label);
    }
}
