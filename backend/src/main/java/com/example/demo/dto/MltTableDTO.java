package com.example.demo.dto;


import java.util.List;

public class MltTableDTO {
    private List<MltRowDTO> Mlt;

    public MltTableDTO(List<MltRowDTO> dt) {
        this.Mlt = dt;
    }

    public List<MltRowDTO> getMlt() { return this.Mlt; }
    public void setMlt(List<MltRowDTO> d) { this.Mlt = d; }
}
