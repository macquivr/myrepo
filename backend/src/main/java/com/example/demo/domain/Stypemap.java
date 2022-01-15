package com.example.demo.domain;

import javax.persistence.*;

@Entity
public class Stypemap {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "csbtype")
    private Csbtype csbtype;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "label")
    private Label label;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payee")
    private Payee payee;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stype")
    private Stype stype;

    public int getId() { return id; }

    public Csbtype getCsbType() { return csbtype; }
    public Label getLabel() { return label; }
    public Payee getPayee() { return payee; }
    public Stype getStype() { return stype; }

    public void setLabel(Label l) { label = l; }
    public void setStype(Stype s) { stype = s; }
}