package com.example.demo.dto;


import java.util.List;

public class LedgerTableDTO {
    private List<LedgerRowDTO> Ledger;

    public LedgerTableDTO(List<LedgerRowDTO> dt) {
        Ledger = dt;
    }

    public List<LedgerRowDTO> getLedger() { return Ledger; }
    public void setLedger(List<LedgerRowDTO> d) { Ledger = d; }
}
