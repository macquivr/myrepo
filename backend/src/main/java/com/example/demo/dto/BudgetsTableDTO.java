package com.example.demo.dto;

import java.util.List;

public class BudgetsTableDTO {
    private List<BudgetsRowDTO> budget;

    public BudgetsTableDTO(List<BudgetsRowDTO> dt) {
        this.budget = dt;
    }

    public List<BudgetsRowDTO> getBudgets() { return this.budget; }
    public void setBudget(List<BudgetsRowDTO> d) { this.budget = d; }
}
