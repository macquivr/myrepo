package com.example.demo.utils;

import com.example.demo.domain.Ledger;
import java.util.Comparator;

public class LedgerDataComparator implements Comparator<Ledger>
{
    public int compare(Ledger o1, Ledger o2) {
        if (o1.getTransdate() == null || o2.getTransdate() == null)
            return 0;
        return o1.getTransdate().compareTo(o2.getTransdate());
    }
}

