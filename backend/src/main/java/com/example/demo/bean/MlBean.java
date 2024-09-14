package com.example.demo.bean;

import com.example.demo.domain.Ltype;
import com.example.demo.domain.Statements;

import java.time.LocalDate;

public class MlBean {
    private double amt;
    private Statements stmts;
    private LocalDate dt;
    private Ltype ltype;

    public void setAmt(double d) {
        this.amt = d;
    }

    public void setStatements(Statements sts) {
        this.stmts = sts;
    }

    public void setLocalDate(LocalDate d) {
        this.dt = d;
    }

    public void setLtype(Ltype l) {
        this.ltype = l;
    }

    public double getAmt() { return this.amt; }
    public Statements getStmts() { return this.stmts; }
    public LocalDate getDate() { return this.dt; }
    public Ltype getLtype() { return this.ltype; }
}
