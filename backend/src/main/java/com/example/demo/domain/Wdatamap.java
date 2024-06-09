package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Wdatamap {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDate tdate;

    // who kvp
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "who")
    private Kvp who;

    // freq kvp
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "freq")
    private Kvp freq;

    private int credit;

    // wid payperiod
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wid")
    private Payperiod wid;

    // tid tledger
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tid")
    private TLedger tid;

    // other kvp
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "other")
    private Kvp other;

    public int getId() { return id; }

    public LocalDate getTdate() { return this.tdate; }
    public Kvp getWho() { return this.who; }
    public Kvp getFreq() { return this.freq; }
    public int getCredit() { return this.credit; }
    public Payperiod getWid() { return this.wid; }
    public TLedger getTid() { return this.tid; }
    public Kvp getOther() { return this.other; }

    public void setTdate(LocalDate d) { tdate = d; }
    public void setWho(Kvp w) { this.who = w; }
    public void setFreq(Kvp f) { this.freq = f; }
    public void setCredit(int c) { this.credit = c; }
    public void setWid(Payperiod p) { this.wid = p; }
    public void setTid(TLedger t) { this.tid = t; }
    public void setOther(Kvp o) { this.other = o; }


}
