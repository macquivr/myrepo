package com.example.demo.dto;

import java.util.List;

public class BudgetValueTableDTO {
    private List<BudgetValueRowDTO> budgetvalues;

    public BudgetValueTableDTO(List<BudgetValueRowDTO> dt) {
        this.budgetvalues = dt;
    }

    public List<BudgetValueRowDTO> getBudgetvalues() { return this.budgetvalues; }
    public void setBudgetvalues(List<BudgetValueRowDTO> d) { this.budgetvalues = d; }
}
