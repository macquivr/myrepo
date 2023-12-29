package com.example.demo.utils.idate;

import com.example.demo.domain.Ledger;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.dvi.Ldvi;

import java.time.LocalDate;

public class LedgerIDate implements Idate {
    private Ledger ledger;

    public LedgerIDate(Ledger l) { ledger = l; }

    public LocalDate getDate() { return ledger.getTransdate(); }
    public Dvi getData() { return new Ldvi(this.ledger); }
}
