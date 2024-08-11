package com.example.demo.dto;

import com.example.demo.domain.Pptlm;

import java.util.List;

public class PptlmDTO {
    private List<Pptlm> data;

    public PptlmDTO(List<Pptlm> dt) {
        this.data = dt;
    }

    public List<Pptlm> getPptlm() { return this.data; }
    public void setPptlm(List<Pptlm> d) { this.data = d; }
}

