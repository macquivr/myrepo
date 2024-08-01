package com.example.demo.dto;

import com.example.demo.bean.RValue;
import com.example.demo.domain.Stype;

import java.util.List;

public class RValueDTO {
    private List<RValue> rvalue;

    public RValueDTO(List<RValue> dt) {

        this.rvalue = dt;
    }

    public List<RValue> getRvalue() {
        return this.rvalue;
    }
    public void setRvalue(List<RValue> d) {
        this.rvalue = d;
    }
}
