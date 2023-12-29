package com.example.demo.dto;

import com.example.demo.domain.Budgetvalues;

import java.time.LocalDate;

public class BudgetValueRowDTO {

    private String name;
    private double value;

    private LocalDate created;
    private LocalDate lastUsed;

    public BudgetValueRowDTO() { /* npo */ }
    public BudgetValueRowDTO(Budgetvalues data) {

        this.value = data.getValue();
        this.name = data.getName();
        this.created = data.getCreated();
        this.lastUsed = data.getLastUsed();
    }

    public void setValue(double v) { this.value = v; }

    public void setName(String n) { this.name = n; }

    public void setCreated(LocalDate d) { this.created = d; }
    public void setLastUsed(LocalDate d) { this.lastUsed = d; }



    public double getValue() { return this.value; }

    public String getName() { return this.name; }

    public LocalDate getCreated() { return this.created; }
    public LocalDate getLastUsed() { return this.lastUsed; }
}
