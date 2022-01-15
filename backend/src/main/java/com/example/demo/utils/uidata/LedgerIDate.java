package com.example.demo.utils.uidata;

import com.example.demo.domain.Ledger;
import java.time.LocalDate;

public class LedgerIDate implements Idate {
    private Ledger ledger;

    public LedgerIDate(Ledger l) { ledger = l; }

    public LocalDate getDate() { return ledger.getTransdate(); }
    public Object getData() { return ledger; }
}
