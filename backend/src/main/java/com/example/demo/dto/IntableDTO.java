package com.example.demo.dto;

import com.example.demo.domain.Intable;

import java.util.List;

public class IntableDTO {
    private List<Intable> intable;

    public IntableDTO(List<Intable> dt) {
        intable = dt;
    }

    public List<Intable> getIntable() { return this.intable; }
    public void setIntable(List<Intable> d) { this.intable = d; }
}
