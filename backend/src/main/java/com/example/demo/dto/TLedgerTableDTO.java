package com.example.demo.dto;


import java.util.List;

public class TLedgerTableDTO {
    private List<TLedgerRowDTO> ledger;

    public TLedgerTableDTO(List<TLedgerRowDTO> dt) {
        this.ledger = dt;
    }

    public List<TLedgerRowDTO> getTLedger() {
        return this.ledger;
    }

    public void setTLedger(List<TLedgerRowDTO> d) {
        this.ledger = d;
    }
}
