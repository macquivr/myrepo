package com.example.demo.dto;

import com.example.demo.domain.Mlt;

import java.time.LocalDate;

public class MltRowDTO {
    private LocalDate dt;

    private double ina;
    private double outa;
    private double balance;

    public MltRowDTO() { /* npo */ }
    public MltRowDTO(Mlt data) {
        if (data == null) {
            this.dt = LocalDate.now();
            this.ina = 0;
            this.outa = 0;
            this.balance = 0;
            return;
        }
        this.dt = (data.getDt() == null) ? LocalDate.now() : data.getDt();
        this.ina = (data.getIna() == null) ? 0 : data.getIna();
        this.outa = (data.getOuta() == null) ? 0 : data.getOuta();
        this.balance = (data.getBalance() == null) ? 0 : data.getBalance();
    }

    public void setDt(LocalDate d) { this.dt = d; }
    public void setIna(double v) { this.ina = v; }
    public void setOuta(double v) { this.outa = v; }
    public void setBalance(double v) { this.balance = v; }

    public LocalDate getDt() { return this.dt; }
    public double getIna() { return this.ina; }
    public double getOuta() { return this.outa; }
    public double getBalance() { return this.balance; }
}
