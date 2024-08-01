package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Intable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private double work;
    private double mlsale;
    private double mldividend;
    private double mlin;
    private double arizona;
    private double miscin;
    private double interest;
    private double refund;
    private double total;

    public int getId() { return id; }
    public double getWork()
    {
        return this.work;
    }
    public double getMlsale() { return this.mlsale; }
    public double getMldividend()
    {
        return this.mldividend;
    }
    public double getMlin() { return this.mlin; }
    public double getArizona()
    {
        return this.arizona;
    }
    public double getMiscin() { return this.miscin; }
    public double getInterest()
    {
        return this.interest;
    }
    public double getRefund() { return this.refund; }
    public double getTotal() { return this.total; }


    public void setWork(double w) { this.work = w; }
    public void setMlsale(double m) { this.mlsale = m; }
    public void setMldividend(double m) { this.mldividend = m; }
    public void setMlin(double m) { this.mlin = m; }
    public void setArizona(double a) { this.arizona = a; }
    public void setMiscin(double m) { this.miscin = m; }
    public void setInterest(double i) { this.interest = i; }
    public void setRefund(double r) { this.refund = r; }
    public void setTotal(double t) { this.total = t; }
}
