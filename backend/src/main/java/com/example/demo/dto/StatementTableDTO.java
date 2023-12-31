package com.example.demo.dto;

import java.util.List;

public class StatementTableDTO {
    private List<StatementRowDTO> Statement;

    public StatementTableDTO(List<StatementRowDTO> dt) {
        Statement = dt;
    }

    public List<StatementRowDTO> getStatement() { return Statement; }
    public void setStatement(List<StatementRowDTO> d) { Statement = d; }
}
