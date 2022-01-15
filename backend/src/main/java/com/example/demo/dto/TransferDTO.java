package com.example.demo.dto;

import com.example.demo.domain.Transfer;

import java.util.List;

public class TransferDTO {
    private List<Transfer> Transfer;

    public TransferDTO(List<Transfer> dt) {
        Transfer = dt;
    }

    public List<Transfer> getTransfer() { return Transfer; }
    public void setTransfer(List<Transfer> d) { Transfer = d; }
}
