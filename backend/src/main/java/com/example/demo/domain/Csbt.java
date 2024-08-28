package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Csbt {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDate dt;
    private Double ina;
    private Double outa;
    private Double balance;


    public int getId() { return this.id; }
    public Double getIna() { return this.ina; }
    public Double getOuta() { return this.outa; }
    public Double getBalance() { return this.balance; }
    public LocalDate getDt() { return this.dt; }

    public void setIna(Double d) { this.ina = d; }
    public void setOuta(Double d) { this.outa = d; }
    public void setBalance(Double d) { this.balance = d; }
    public void setDt(LocalDate d) { this.dt = d; }

}
