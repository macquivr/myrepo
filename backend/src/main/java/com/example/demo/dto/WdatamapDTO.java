package com.example.demo.dto;

import com.example.demo.domain.Kvp;
import com.example.demo.domain.Wdatamap;

import java.util.List;

public class WdatamapDTO {
    private List<Wdatamap> data;

    public WdatamapDTO(List<Wdatamap> dt) {
        this.data = dt;
    }

    public List<Wdatamap> getWdatamap() { return this.data; }
    public void setWdatamap(List<Wdatamap> d) { this.data = d; }
}
