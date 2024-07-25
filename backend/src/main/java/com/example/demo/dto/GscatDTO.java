package com.example.demo.dto;

import com.example.demo.domain.Gscat;

import java.util.List;

public class GscatDTO {
    private List<Gscat> gscats;

    public GscatDTO(List<Gscat> dt) {
        gscats = dt;
    }

    public List<Gscat> getGscat() { return this.gscats; }
    public void setGscat(List<Gscat> d) { this.gscats = d; }
}
