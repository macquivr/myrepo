package com.example.demo.repository;

import com.example.demo.domain.Statement;
import com.example.demo.domain.Statements;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.Ltype;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Integer> {
    Statement findAllByIdAndLtype(int id, Ltype type);
    Statement findAllByStatementsAndLtype(Statements stmt, Ltype type);
    List<Statement> findAllByStatements(Statements stmt);

    List<Statement> findAllByLtypeOrderByStatements(Ltype type);
}
