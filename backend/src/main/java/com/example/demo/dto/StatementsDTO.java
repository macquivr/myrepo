package com.example.demo.dto;

import com.example.demo.domain.Statements;

import java.util.List;

public class StatementsDTO {
    private List<Statements> Statements;

    public StatementsDTO(List<Statements> dt) {
        Statements = dt;
    }

    public List<Statements> getStatements() { return Statements; }
    public void setStatements(List<Statements> d) { Statements = d; }
}
