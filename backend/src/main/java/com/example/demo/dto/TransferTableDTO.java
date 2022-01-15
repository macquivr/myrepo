package com.example.demo.dto;

import java.util.List;

public class TransferTableDTO {
    private List<TransferRowDTO> Transfer;

    public TransferTableDTO(List<TransferRowDTO> dt) {
        Transfer = dt;
    }

    public List<TransferRowDTO> getTransfer() { return Transfer; }
    public void setTransfer(List<TransferRowDTO> d) { Transfer = d; }
}
