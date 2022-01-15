package com.example.demo.dto;

import com.example.demo.domain.Names;

import java.util.List;

public class NamesDTO {
    private List<Names> Names;

    public NamesDTO(List<Names> dt) {
        Names = dt;
    }

    public List<Names> getNames() { return Names; }
    public void setNames(List<Names> d) { Names = d; }
}
