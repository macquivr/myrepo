package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Payperiod {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDate start;
    private LocalDate stop;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ina")
    private Intable ina;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outa")
    private Outtable outa;

    public int getId() { return this.id; }
    public LocalDate getStart() { return start; }
    public LocalDate getStop() { return stop; }
    public Intable getIna() { return this.ina; }
    public Outtable getOuta() { return this.outa; }

    public void setStart(LocalDate d) { start = d; }
    public void setStop(LocalDate d) { stop = d; }
    public void setIna(Intable ina) { this.ina = ina; }
    public void setOuta(Outtable outa) { this.outa = outa; }
}
