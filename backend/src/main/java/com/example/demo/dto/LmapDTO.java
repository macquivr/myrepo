package com.example.demo.dto;

import com.example.demo.domain.Lmap;

import java.util.List;

public class LmapDTO {
    private List<Lmap> lmaps;

    public LmapDTO(List<Lmap> dt) {
        lmaps = dt;
    }

    public List<Lmap> getLmap() { return this.lmaps; }
    public void setLmap(List<Lmap> d) { this.lmaps = d; }
}
