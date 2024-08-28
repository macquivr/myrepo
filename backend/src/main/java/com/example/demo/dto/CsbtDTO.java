package com.example.demo.dto;

import com.example.demo.domain.Csbt;

import java.util.List;

public class CsbtDTO {
    private List<Csbt> csbt;

    public CsbtDTO(List<Csbt> dt) {
        this.csbt = dt;
    }

    public List<Csbt> getCsbt() { return this.csbt; }
    public void setCsbt(List<Csbt> d) { this.csbt = d; }
}
