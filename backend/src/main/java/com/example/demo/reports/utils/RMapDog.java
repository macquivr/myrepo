package com.example.demo.reports.utils;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Ledger;

public class RMapDog implements RMapI {

    public boolean apply(Ledger data) {
        if (data.getChecks() != null) {
            Checks c = data.getChecks();
            if (c.getPayee().getName().equals("dog")) {
                return true;
            }
        }
        return (data.getLabel().getId() == 13137);
    }
}
