package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDate bdate;
    private Double value;
    private Double net;

    private LocalDate created;

    private LocalDate lastUsed;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stmts")
    private Statements stmts;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bid")
    private Budgetvalues bid;

    public int getId() { return this.id; }
    public Double getValue() { return this.value; }
    public Double getNet() { return this.net; }
    public LocalDate getBdate() { return this.bdate; }
    public Budgetvalues getBid() { return this.bid; }
    public Statements getStmts() { return stmts; }

    public LocalDate getCreated() { return this.created; }

    public LocalDate getLastUsed() { return this.lastUsed; }
    public void setValue(Double d) { this.value = d; }
    public void setNet(Double d) { this.net = d; }
    public void setBdate(LocalDate d) { this.bdate = d; }
    public void setBid(Budgetvalues b) { this.bid = b; }
    public void setStmts(Statements s) { this.stmts = s; }

    public void setCreated(LocalDate d) { this.created = d; }

    public void setLastUsed(LocalDate d) { this.lastUsed = d; }
}
