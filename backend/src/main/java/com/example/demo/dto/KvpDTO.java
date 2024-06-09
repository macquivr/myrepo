package com.example.demo.dto;

import com.example.demo.domain.Kvp;
import com.example.demo.domain.Payperiod;

import java.util.List;

public class KvpDTO {
    private List<Kvp> data;

    public KvpDTO(List<Kvp> dt) {
        this.data = dt;
    }

    public List<Kvp> getKvp() { return this.data; }
    public void setKvp(List<Kvp> d) { this.data = d; }
}

