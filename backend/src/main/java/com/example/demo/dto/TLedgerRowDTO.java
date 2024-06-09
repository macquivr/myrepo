package com.example.demo.dto;

import java.time.LocalDate;
import com.example.demo.domain.TLedger;

public class TLedgerRowDTO {
    private LocalDate tdate;
    private String account;
    private String label;
    private String location;
    private String category;
    private double amount;
    private int checkNumber;
    private String checkPayee;
    private String ltype;

    public TLedgerRowDTO() { /* npo */ }
    public TLedgerRowDTO(TLedger data) {
        try {
            tdate = data.getTdate();
            account = data.getLtype().getName();
            label = data.getLabel().getNames().getName();
            location = data.getLabel().getLocation().getName();
            category = data.getLabel().getCategory().getName();
            amount = data.getAmount();
            ltype = data.getLtype().getName();
            checkNumber = (data.getChecks() != null) ? data.getChecks().getCheckNum() : 0;
            checkPayee = (data.getChecks() != null) ? data.getChecks().getPayee().getName() : "None";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTdate(LocalDate d) { tdate = d; }
    public void setAccount(String a) { account = a; }
    public void setLabel(String l) { label = l; }
    public void setLocation(String l) { location = l; }
    public void setCategory(String c) { category = c; }
    public void setAmount(double a) { amount = a; }
    public void setLtype(String l) { ltype = l; }
    public void setCheckNumber(int c) { checkNumber = c; }
    public void setCheckPayee(String p) { checkPayee = p; }

    public LocalDate getTdate() { return tdate; }
    public String getAccount() { return account; }
    public String getLabel() { return label;}
    public String getLocation() { return location;}
    public String getCategory() { return category;}
    public double getAmount() { return amount; }
    public int getCheckNumber() { return checkNumber; }
    public String getCheckPayee() { return checkPayee; }

    public String getLtype() { return ltype; }
}
