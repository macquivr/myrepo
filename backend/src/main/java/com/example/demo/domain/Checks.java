package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Checks {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private LocalDate checkDate;
    private int checkNum;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ltype")
    private Ltype ltype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payee")
    private Payee payee;

    public int getId() { return id; }
    public LocalDate getCheckDate() { return checkDate; }
    public int getCheckNum() { return checkNum; }
    public Ltype getLtype() { return ltype; }
    public Payee getPayee() { return payee; }

    public void setCheckDate(LocalDate d) { checkDate = d; }
    public void setCheckNum(int n) { checkNum = n; }
    public void setLtype(Ltype l) { ltype = l; }
    public void setPayee(Payee p) { payee = p; }
}
