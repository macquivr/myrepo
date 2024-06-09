package com.example.demo.dto;

import java.util.List;

public class WdatamapTableDTO {
    private List<WdatamapRowDTO> rowdata;

    public WdatamapTableDTO(List<WdatamapRowDTO> dt) {
        this.rowdata = dt;
    }

    public List<WdatamapRowDTO> getWdatamap() { return this.rowdata; }
    public void setWdatamap(List<WdatamapRowDTO> d) { this.rowdata = d; }
}
