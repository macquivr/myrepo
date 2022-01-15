package com.example.demo.domain;

import javax.persistence.*;

@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_label")
    private Label fromLabel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_label")
    private Label toLabel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_payee")
    private Payee fromPayee;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_ltype")
    private Ltype fromLtype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_ltype")
    private Ltype toLtype;

    public int getId() { return id; }

    public Ltype getFromLtype() { return fromLtype; }
    public Label getFromLabel() { return fromLabel; }
    public Ltype getToLType() { return toLtype; }
    public Label getToLabel() { return toLabel; }
    public Payee getFromPayee() { return fromPayee; }
}
