package com.example.demo.dto.ui;

import java.util.List;

public class BillsTableDTO {
    private List<BillsRowDTO> bills;

    public BillsTableDTO() { /* nop */ }

    public BillsTableDTO(List<BillsRowDTO> dt) {
        bills = dt;
    }

    public List<BillsRowDTO> getBills() { return bills; }
    public void setBills(List<BillsRowDTO> d) { bills = d; }
}
