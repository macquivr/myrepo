package com.example.demo.dto.ui;

import java.util.List;

public class BalanceTableDTO {
    private List<BalanceRowDTO> balance;

    public BalanceTableDTO() { /* nop */ }

    public BalanceTableDTO(List<BalanceRowDTO> dt) {
        balance = dt;
    }

    public List<BalanceRowDTO> getBalance() { return balance; }
    public void setBalance(List<BalanceRowDTO> d) { balance = d; }
}
