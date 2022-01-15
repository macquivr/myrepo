package com.example.demo.dto;

import com.example.demo.domain.Mltype;

import java.util.List;

public class MltypeDTO {
    private List<Mltype> Mltypes;

    public MltypeDTO(List<Mltype> dt) {
        Mltypes = dt;
    }

    public List<Mltype> getMltypes() { return Mltypes; }
    public void setMltypes(List<Mltype> d) { Mltypes = d; }
}
