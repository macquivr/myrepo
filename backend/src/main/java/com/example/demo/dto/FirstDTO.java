package com.example.demo.dto;

import com.example.demo.domain.First;

import java.util.List;

public class FirstDTO {
    private List<First> First;

    public FirstDTO(List<First> dt) {
        First = dt;
    }

    public List<First> getFirst() { return First; }
    public void setFirst(List<First> d) { First = d; }
}
