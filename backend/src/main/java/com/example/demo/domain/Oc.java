package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Oc {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDate sdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ltype")
    private Ltype ltype;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stmt")
    private Statement stmt;

    private double owed;
    private double paid;

    private double newa;

    private double debt;

    private double fc;

    private double fee;

    private double free;

    private double ocv;

    private double dr;

    private double overv;

    private double under;
    public int getId() { return this.id; }
    public LocalDate getSdate() { return this.sdate; }
    public void setSdate(LocalDate d) { this.sdate = d; }

    public Ltype getLtype() { return this.ltype; }
    public void setLtype(Ltype l) { this.ltype = l; }

    public Statement getStmt() { return this.stmt; }
    public void setStmt(Statement s) { this.stmt = s; }

    public double getOwed() { return this.owed; }
    public void setOwed(double d) { this.owed = d; }

    public double getPaid() { return this.paid; }
    public void setPaid(double d) { this.paid = d; }

    public double getNewa() { return this.newa; }
    public void setNewa(double d) { this.newa = d; }

    public double getDebt() { return this.debt; }
    public void setDebt(double d) { this.debt = d; }

    public double getFc() { return this.fc; }
    public void setFc(double d) { this.fc = d; }


    public double getFree() { return this.free; }
    public void setFree(double d) { this.free = d; }

    public double getFee() { return this.fee; }
    public void setFee(double d) { this.fee = d; }

    public double getOcv() { return this.ocv; }
    public void setOcv(double d) { this.ocv = d; }

    public double getDr() { return this.dr; }
    public void setDr(double d) { this.dr = d; }

    public double getOverv() { return this.overv; }
    public void setOverv(double d) { this.overv = d; }

    public double getUnder() { return this.under; }
    public void setUnder(double d) { this.under = d; }

}
