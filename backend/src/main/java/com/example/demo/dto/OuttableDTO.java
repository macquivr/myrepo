package com.example.demo.dto;

import com.example.demo.domain.Outtable;

import java.util.List;

public class OuttableDTO {
    private List<Outtable> outtable;

    public OuttableDTO(List<Outtable> dt) {
        outtable = dt;
    }

    public List<Outtable> getOuttable() { return this.outtable; }
    public void setOuttable(List<Outtable> d) { this.outtable = d; }
}
