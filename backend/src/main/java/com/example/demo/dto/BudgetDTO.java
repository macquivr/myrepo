package com.example.demo.dto;

import com.example.demo.domain.Budget;

import java.util.List;

public class BudgetDTO {
    private List<Budget> budget;

    public BudgetDTO(List<Budget> dt) {
        this.budget = dt;
    }

    public List<Budget> getBudget() { return this.budget; }
    public void setBudget(List<Budget> d) { this.budget = d; }
}
