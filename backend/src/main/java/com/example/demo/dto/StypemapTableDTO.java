package com.example.demo.dto;

import com.example.demo.domain.Stypemap;

import java.util.List;

public class StypemapTableDTO {
    private List<StypemapRowDTO> Stypemap;

    public StypemapTableDTO(List<StypemapRowDTO> dt) {
        Stypemap = dt;
    }

    public List<StypemapRowDTO> getStypemap() { return Stypemap; }
    public void setStypemap(List<StypemapRowDTO> d) { Stypemap = d; }
}
