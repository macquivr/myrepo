package com.example.demo.dto;


import java.util.List;

public class CsbtTableDTO {
    private List<CsbtRowDTO> csbt;

    public CsbtTableDTO(List<CsbtRowDTO> dt) {
        this.csbt = dt;
    }

    public List<CsbtRowDTO> getCsbt() { return this.csbt; }
    public void setCsbt(List<CsbtRowDTO> d) { this.csbt = d; }
}
