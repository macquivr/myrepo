package com.example.demo.dto;

import com.example.demo.domain.Budget;

import java.util.List;

public class BudgetTableDTO {
    private List<BudgetRowDTO> budget;

    public BudgetTableDTO(List<BudgetRowDTO> dt) {
        this.budget = dt;
    }

    public List<BudgetRowDTO> getBudget() { return this.budget; }
    public void setBudget(List<BudgetRowDTO> d) { this.budget = d; }
}
