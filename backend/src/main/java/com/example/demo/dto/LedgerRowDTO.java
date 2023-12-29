package com.example.demo.dto;

import java.time.LocalDate;
import com.example.demo.domain.Ledger;

public class LedgerRowDTO {
    private LocalDate transdate;
    private String account;
    private String label;
    private String location;
    private String category;
    private double amount;
    private int checkNumber;
    private String checkPayee;
    private String statement;
    private String stype;

    public LedgerRowDTO() { /* npo */ }
    public LedgerRowDTO(Ledger data) {
        try {
            transdate = data.getTransdate();
            account = data.getLtype().getName();
            label = data.getLabel().getNames().getName();
            location = data.getLabel().getLocation().getName();
            category = data.getLabel().getCategory().getName();
            amount = data.getAmount();
            checkNumber = (data.getChecks() != null) ? data.getChecks().getCheckNum() : 0;
            checkPayee = (data.getChecks() != null) ? data.getChecks().getPayee().getName() : "None";
            statement = (data.getStatement() != null) ? data.getStatement().getStatements().getName() : "None";
            stype = data.getStype().getName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTransdate(LocalDate d) { transdate = d; }
    public void setAccount(String a) { account = a; }
    public void setLabel(String l) { label = l; }
    public void setLocation(String l) { location = l; }
    public void setCategory(String c) { category = c; }
    public void setAmount(double a) { amount = a; }
    public void setCheckNumber(int c) { checkNumber = c; }
    public void setCheckPayee(String p) { checkPayee = p; }
    public void setStatement(String s) { statement = s; }
    public void setStype(String s) { stype = s; }

    public LocalDate getTransdate() { return transdate; }
    public String getAccount() { return account; }
    public String getLabel() { return label;}
    public String getLocation() { return location;}
    public String getCategory() { return category;}
    public double getAmount() { return amount; }
    public int getCheckNumber() { return checkNumber; }
    public String getCheckPayee() { return checkPayee; }
    public String getStatement() { return statement; }
    public String getStype() { return stype; }
}
