package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Ledger {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDate transdate;
    private Double amount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ltype")
    private Ltype ltype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "label")
    private Label label;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stype")
    private Stype stype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checks")
    private Checks checks;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statement")
    private Statement statement;

    public int getId() { return id; }
    public Double getAmount() { return amount; }
    public LocalDate getTransdate() { return transdate; }
    public Ltype getLtype() { return ltype; }
    public Label getLabel() { return label; }
    public Stype getStype() { return stype; }
    public Checks getChecks() { return checks; }
    public Statement getStatement() { return statement; }

    public void setAmount(Double d) { amount = d; }
    public void setTransdate(LocalDate d) { transdate = d; }
    public void setLtype(Ltype l) { ltype = l; }
    public void setLabel(Label l) { label = l; }
    public void setStype(Stype s) { stype = s; }
    public void setChecks(Checks c) { checks = c; }
    public void setStatement(Statement s) { statement = s; }
}
