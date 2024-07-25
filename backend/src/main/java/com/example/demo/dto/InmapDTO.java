package com.example.demo.dto;

import com.example.demo.domain.Inmap;

import java.util.List;

public class InmapDTO {
    private List<Inmap> inmaps;

    public InmapDTO(List<Inmap> dt) {
        inmaps = dt;
    }

    public List<Inmap> getInmap() { return this.inmaps; }
    public void setInmap(List<Inmap> d) { this.inmaps = d; }
}
