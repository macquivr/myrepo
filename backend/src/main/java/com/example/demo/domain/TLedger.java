package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class TLedger {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDate tdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "label")
    private Label label;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checks")
    private Checks checks;

    private Double amount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ltype")
    private Ltype ltype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wid")
    private Payperiod wid;

    public int getId() { return id; }

    public Double getAmount() { return amount; }
    public LocalDate getTdate() { return tdate; }
    public Ltype getLtype() { return ltype; }
    public Label getLabel() { return label; }
    public Checks getChecks() { return checks; }
    public Payperiod getPayperiod() { return wid; }

    public void setAmount(Double d) { amount = d; }
    public void setTdate(LocalDate d) { tdate = d; }
    public void setLtype(Ltype l) { ltype = l; }
    public void setLabel(Label l) { label = l; }
    public void setChecks(Checks c) { checks = c; }
    public void setPayperiod(Payperiod p) { wid = p; }
}
