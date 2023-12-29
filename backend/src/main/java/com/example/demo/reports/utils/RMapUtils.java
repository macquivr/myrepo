package com.example.demo.reports.utils;

import com.example.demo.domain.Ledger;

public class RMapUtils implements RMapI {

    public boolean apply(Ledger data) {
        if (data.getLabel().getId() == 10344)
            return true;
        if (data.getChecks() == null)
            return false;
        int p = data.getChecks().getPayee().getId();
        return ((p == 60) || (p == 75) || (p == 64));
    }
}
