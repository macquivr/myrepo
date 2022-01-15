package com.example.demo.dto;

import com.example.demo.domain.Statement;

import java.util.List;

public class StatementDTO {
    private List<Statement> Statement;

    public StatementDTO(List<Statement> dt) {
        Statement = dt;
    }

    public List<Statement> getStatement() { return Statement; }
    public void setStatement(List<Statement> d) { Statement = d; }
}
