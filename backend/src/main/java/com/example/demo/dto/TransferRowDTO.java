package com.example.demo.dto;

import com.example.demo.domain.Transfer;

public class TransferRowDTO {
    private String fromLabel;
    private String fromAccount;
    private String fromPayee;
    private String toLabel;
    private String toAccount;

    public TransferRowDTO() { /* npo */ }
    public TransferRowDTO(Transfer data) {
        fromLabel = (data.getFromLabel() != null) ? data.getFromLabel().getName() : "N/A";
        fromAccount = (data.getFromLtype() != null) ? data.getFromLtype().getName() : "N/A";
        fromPayee = (data.getFromPayee() != null) ? data.getFromPayee().getName() : "N/A";
        toLabel = (data.getToLabel() != null) ? data.getToLabel().getName() : "N/A";
        toAccount = (data.getToLType() != null) ? data.getToLType().getName() : "N/A";
    }

    public void setFromLabel(String n) { fromLabel = n; }
    public void setFromAccount(String s) { fromAccount = s; }
    public void setFromPayee(String s) { fromPayee = s; }
    public void setToLabel(String d) { toLabel = d; }
    public void setToAccount(String d) { toAccount = d; }

    public String getFromLabel() { return fromLabel; }
    public String getFromAccount() { return fromAccount;}
    public String getFromPayee() { return fromPayee; }
    public String getToLabel() { return toLabel; }
    public String getToAccount() { return toAccount; }
}
