package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Budgetvalues {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double value;

    private LocalDate created;

    private LocalDate lastUsed;

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public Double getValue() { return this.value; }

    public LocalDate getCreated() { return this.created; }

    public LocalDate getLastUsed() { return this.lastUsed; }
    public void setName(String n) { this.name = n; }
    public void setValue(Double d) { this.value = d; }

    public void setCreated(LocalDate d) { this.created = d; }

    public void setLastUsed(LocalDate d) { this.lastUsed = d; }
}
