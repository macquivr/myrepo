package com.example.demo.dto;

import com.example.demo.domain.Budgets;

import java.time.LocalDate;

public class BudgetsRowDTO {
    private LocalDate bdate;
    private int bid;

    private double value;

    private double net;

    private int stmts;

    private LocalDate created;
    private LocalDate lastUsed;

    public BudgetsRowDTO() { /* npo */ }
    public BudgetsRowDTO(Budgets data) {
        this.bdate = data.getBdate();
        this.bid = data.getBid().getId();
        this.value = data.getValue();
        this.net = data.getNet();
        this.stmts = data.getStmts().getId();
        this.created = data.getCreated();
        this.lastUsed = data.getLastUsed();
    }

    public void setBdate(LocalDate d) { this.bdate = d; }

    public void setBid(int b) { this.bid = b; }

    public void setValue(double v) { this.value = v; }

    public void setNet(double v) { this.net = v; }

    public void setStmts(int i) { this.stmts = i; }

    public void setCreated(LocalDate d) { this.created = d; }
    public void setLastUsed(LocalDate d) { this.lastUsed = d; }

    public LocalDate getBdate() { return this.bdate; }

    public int getBid() { return this.bid; }

    public double getValue() { return this.value; }

    public double getNet() { return this.net; }

    public int getStmts() { return this.stmts; }

    public LocalDate getCreated() { return this.created; }
    public LocalDate getLastUsed() { return this.lastUsed; }
}
