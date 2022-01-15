package com.example.demo.dto.ui;

import java.util.List;

public class CreditTableDTO {
    private List<CreditRowDTO> credit;

    public CreditTableDTO() { /* nop */ }

    public CreditTableDTO(List<CreditRowDTO> dt) {
        credit = dt;
    }

    public List<CreditRowDTO> getCredit() { return credit; }
    public void setCredit(List<CreditRowDTO> d) { credit = d; }
}
