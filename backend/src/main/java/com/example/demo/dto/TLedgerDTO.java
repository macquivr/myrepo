package com.example.demo.dto;

import com.example.demo.domain.TLedger;

import java.util.List;

public class TLedgerDTO {
    private List<TLedger> ledger;

    public TLedgerDTO(List<TLedger> dt) {
        this.ledger = dt;
    }

    public List<TLedger> getTLedger() { return this.ledger; }
    public void setTLedger(List<TLedger> d) { this.ledger = d; }
}
