package com.example.demo.bean;

import com.example.demo.utils.Utils;

public class GBean {
    private double totalOut;
    private double workIn;
    private double nonWorkIn;
    private double creditOut;
    private double utils;
    private double fees;
    private double free;
    private double unpaid;
    private double budgetNet;
    private double balance;
    private double annual;

    public double getTotalIn() {
        return Utils.convertDouble(this.workIn + this.nonWorkIn);
    }

    public double getTotalOut() {
        return this.totalOut;
    }

    public double getTotalNet() {
        return Utils.convertDouble(getTotalIn() + this.totalOut);
    }

    public double getWorkIn() {
        return this.workIn;
    }

    public double getNonWorkIn() {
        return this.nonWorkIn;
    }

    public double getCreditOut() {
        return this.creditOut;
    }

    public double getUtils() { return this.utils; }

    public double getFees() { return this.fees; }

    public double getFree() {
        return this.free;
    }

    public double getNfeeFree() {
        return Utils.convertDouble(this.free + fees);
    }

    public double getUnpaid() {
        return this.unpaid;
    }

    public double getBudgetNet() {
        return this.budgetNet;
    }

    public double getBalance() {
        return this.balance;
    }

    public double getAnnual() { return this.annual; }

    public void setAnnual(double t) {
        this.annual = Utils.convertDouble(t);
    }
    public void  setTotalOut(double t) {
        this.totalOut = Utils.convertDouble(t);
    }

    public void setWorkIn(double t) {
        this.workIn = Utils.convertDouble(t);
    }

    public void setNonworkIn(double t) {
        this.nonWorkIn = Utils.convertDouble(t);
    }

    public void setCreditOut(double t) {
        this.creditOut = Utils.convertDouble(t);
    }

    public void setUtils(double t) {
        this.utils = Utils.convertDouble(t);
    }

    public void setFees(double t) {
        this.fees = Utils.convertDouble(t);
    }

    public void setFree(double t)
    {
        this.free = Utils.convertDouble(t);
    }

    public void setUnpaid(double t) {
        this.unpaid = Utils.convertDouble(t);
    }

    public void setBudgetNet(double t)
    {
        this.budgetNet = Utils.convertDouble(t);
    }

    public void setBalance(double t)
    {
        this.balance = Utils.convertDouble(t);
    }

    public void add (GBean data) {
        setTotalOut(this.totalOut + data.getTotalOut());
        setWorkIn(this.workIn + data.getWorkIn());
        setNonworkIn(this.nonWorkIn + data.getNonWorkIn());
        setCreditOut(this.creditOut + data.getCreditOut());
        setUtils(this.utils + data.getUtils());
        setFees(this.fees + data.getFees());
        setFree(this.free + data.getFree());
        setUnpaid(this.unpaid + data.getUnpaid());
        setBudgetNet(this.budgetNet + data.getBudgetNet());
        setAnnual(this.annual + data.getAnnual());
    }
}