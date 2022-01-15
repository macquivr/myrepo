package com.example.demo.dto;

import com.example.demo.domain.Stype;

import java.util.List;

public class StypeDTO {
    private List<Stype> Stype;

    public StypeDTO(List<Stype> dt) {
        Stype = dt;
    }

    public List<Stype> getStype() { return Stype; }
    public void setStype(List<Stype> d) { Stype = d; }
}
