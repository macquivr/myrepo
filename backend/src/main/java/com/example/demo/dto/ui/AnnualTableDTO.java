package com.example.demo.dto.ui;

import java.util.List;

public class AnnualTableDTO {
    private List<AnnualRowDTO> annual;

    public AnnualTableDTO() { /* nop */ }

    public AnnualTableDTO(List<AnnualRowDTO> dt) {
        annual = dt;
    }

    public List<AnnualRowDTO> getAnnual() { return annual; }
    public void setAnnual(List<AnnualRowDTO> d) { annual = d; }
}
