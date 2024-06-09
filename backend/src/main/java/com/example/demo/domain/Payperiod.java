package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Payperiod {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDate start;
    private LocalDate stop;

    public int getId() { return this.id; }
    public LocalDate getStart() { return start; }
    public LocalDate getStop() { return stop; }

    public void setStart(LocalDate d) { start = d; }
    public void setStop(LocalDate d) { stop = d; }
}
