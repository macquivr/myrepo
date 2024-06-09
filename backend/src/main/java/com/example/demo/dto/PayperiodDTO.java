package com.example.demo.dto;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.TLedger;

import java.util.List;

public class PayperiodDTO {
    private List<Payperiod> data;

    public PayperiodDTO(List<Payperiod> dt) {
        this.data = dt;
    }

    public List<Payperiod> getPayperiod() { return this.data; }
    public void setPayperiod(List<Payperiod> d) { this.data = d; }
}
