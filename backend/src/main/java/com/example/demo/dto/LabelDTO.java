package com.example.demo.dto;

import com.example.demo.domain.Label;

import java.util.List;

public class LabelDTO {
    private List<Label> Label;

    public LabelDTO(List<Label> dt) {
        Label = dt;
    }

    public List<Label> getLabel() { return Label; }
    public void setLabel(List<Label> d) { Label = d; }
}
