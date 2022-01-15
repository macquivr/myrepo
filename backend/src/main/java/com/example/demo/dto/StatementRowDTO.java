package com.example.demo.dto;

import com.example.demo.domain.Statement;
import java.time.LocalDate;

public class StatementRowDTO {
    private LocalDate date;
    private String account;
    private double begin;
    private double end;
    private double ina;
    private double outa;
    private String credit;
    private double fee;

    public StatementRowDTO() { /* nop */ }
    public StatementRowDTO(Statement data) {
        if (data == null)
            return;
        date = data.getStatements().getStmtdate();
        account = data.getLtype().getName();
        begin = data.getSbalance();
        end = data.getFbalance();
        ina = data.getIna();
        outa = data.getOuta();
        credit = data.getCredit() ? "Yes" : "No";
        fee = data.getFee();
    }

    public void setDate(LocalDate n) { date = n; }
    public void setAccount(String a) { account = a; }
    public void setBegin(double b) { begin = b; }
    public void setEnd(double e) { end = e; }
    public void setIna(double i) { ina = i; }
    public void setOuta(double d) { outa = d; }
    public void setCredit(String b) { credit = b; }
    public void setFee(double f) { fee = f; }

    public LocalDate getDate() { return date; }
    public String getAccount() { return account;}
    public double getBegin() { return begin; }
    public double getEnd() { return end; }
    public double getIna() { return ina; }
    public double getOuta() { return outa; }
    public String getCredit() {return credit; }
    public double getFee() { return fee; }
}
