package com.example.demo.dto;

import com.example.demo.domain.Utilities;

import java.util.List;

public class UtilitiesDTO {
    private List<Utilities> Utilities;

    public UtilitiesDTO(List<Utilities> dt) {
        Utilities = dt;
    }

    public List<Utilities> getUtilities() { return Utilities; }
    public void setUtilities(List<Utilities> d) { Utilities = d; }
}
