package com.example.demo.dto.ui;

import com.example.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CStatusTableDTO {
    private List<CStatusRowDTO> cstatus;
    private CStatusRowDTO total;

    public CStatusTableDTO() {
        this.cstatus = new ArrayList<CStatusRowDTO>();
        this.total = new CStatusRowDTO();
        this.total.setName("Total");
    }

    public List<CStatusRowDTO> getCstatus() { return cstatus; }
    public void setCstatus(List<CStatusRowDTO> d) { cstatus = d; }

    public void mtotal() {
        cstatus.add(this.total);
    }
    public void add(CStatusRowDTO data) {
        cstatus.add(data);

        total.setOver(Utils.convertDouble(total.getOver() + data.getOver()));
        total.setUnder(Utils.convertDouble(total.getUnder() + data.getUnder()));
        total.setDr(Utils.convertDouble(total.getDr() + data.getDr()));
        total.setNetfree(Utils.convertDouble(total.getNetfree() + data.getNetfree()));
    }
}
