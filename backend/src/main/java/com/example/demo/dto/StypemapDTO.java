package com.example.demo.dto;

import com.example.demo.domain.Stypemap;

import java.util.List;

public class StypemapDTO {
    private List<Stypemap> Stypemap;

    public StypemapDTO(List<Stypemap> dt) {
        Stypemap = dt;
    }

    public List<Stypemap> getStypemap() { return Stypemap; }
    public void setStypemap(List<Stypemap> d) { Stypemap = d; }
}
