package com.example.demo.dto;

import com.example.demo.domain.Cmap;

import java.util.List;

public class CmapDTO {
    private List<Cmap> cmaps;

    public CmapDTO(List<Cmap> dt) {
        cmaps = dt;
    }

    public List<Cmap> getCmap() { return this.cmaps; }
    public void setCmap(List<Cmap> d) { this.cmaps = d; }
}
