package com.example.demo.reports.utils;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Ledger;

public class RMapSears implements RMapI {

    public boolean apply(Ledger data) {
        if (data.getChecks() != null) {
            Checks c = data.getChecks();
            if (c.getPayee().getName().equals("sears")) {
                return true;
            }
        }
        return (data.getLabel().getId() == 11574);
    }
}
