package com.example.demo.dto;

import com.example.demo.domain.Checktype;

import java.util.List;

public class CheckTypeDTO {
    private List<Checktype> checkTypes;

    public CheckTypeDTO(List<Checktype> dt) {
        checkTypes = dt;
    }

    public List<Checktype> getCheckTypes() { return checkTypes; }
    public void setCheckTypes(List<Checktype> d) { checkTypes = d; }
}
