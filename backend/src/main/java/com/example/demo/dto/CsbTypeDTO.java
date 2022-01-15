package com.example.demo.dto;

import com.example.demo.domain.Csbtype;

import java.util.List;

public class CsbTypeDTO {
    private List<Csbtype> CsbTypes;

    public CsbTypeDTO(List<Csbtype> dt) {
        CsbTypes = dt;
    }

    public List<Csbtype> getCsbTypes() { return CsbTypes; }
    public void setCsbTypes(List<Csbtype> d) { CsbTypes = d; }
}
