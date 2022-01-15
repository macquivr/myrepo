package com.example.demo.dto;

import com.example.demo.domain.Payee;

import java.util.List;

public class PayeeDTO {
    private List<Payee> Payee;

    public PayeeDTO(List<Payee> dt) {
        Payee = dt;
    }

    public List<Payee> getPayee() { return Payee; }
    public void setPayee(List<Payee> d) { Payee = d; }
}
