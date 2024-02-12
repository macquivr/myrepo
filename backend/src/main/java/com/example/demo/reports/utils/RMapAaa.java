package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapAaa implements RMapI {
    private int label = 0;

    public RMapAaa() {
        this.label = 12933;
    }

    public RMapAaa(int l) {
        this.label = l;
    }
    public boolean apply(Ledger data) {
        return (data.getLabel().getId() == this.label);
    }
}
