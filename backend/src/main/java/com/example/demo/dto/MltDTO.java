package com.example.demo.dto;

import com.example.demo.domain.Mlt;

import java.util.List;

public class MltDTO {
    private List<Mlt> Mlt;

    public MltDTO(List<Mlt> dt) {
        this.Mlt = dt;
    }

    public List<Mlt> getMlt() { return this.Mlt; }
    public void setMlt(List<Mlt> d) { this.Mlt = d; }
}
