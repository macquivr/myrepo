package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Outtable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private double outr;
    private double outc;
    private double infree;

    public int getId() { return id; }
    public double getOutr() { return this.outr; }
    public double getOutc() { return this.outc; }
    public double getInfree()
    {
        return this.infree;
    }

    public void setOutr(double w) { this.outr = w; }
    public void setOutc(double m) { this.outc = m; }
    public void setInfree(double m) { this.infree = m; }
}
