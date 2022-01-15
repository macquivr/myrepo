package com.example.demo.dto;

import com.example.demo.domain.Checks;

import java.util.List;

public class ChecksDTO {
    private List<Checks> Checks;

    public ChecksDTO(List<Checks> dt) {
        Checks = dt;
    }

    public List<Checks> getChecks() { return Checks; }
    public void setChecks(List<Checks> d) { Checks = d; }
}
