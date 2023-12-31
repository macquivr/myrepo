package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapRest implements RMapI {
    private final boolean check;

    public RMapRest(boolean c)
    {
        this.check = c;
    }
    public boolean apply(Ledger data) {
        if (data.getStype().getId() == 8)
            return false;

        if (this.check) {
            return (data.getChecks() != null);
        } else {
            return (data.getChecks() == null);
        }
    }
}
