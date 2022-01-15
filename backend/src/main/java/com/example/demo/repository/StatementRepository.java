package com.example.demo.repository;

import com.example.demo.domain.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.Ltype;

public interface StatementRepository extends JpaRepository<Statement, Integer> {
    Statement findAllByIdAndLtype(int id, Ltype type);

}
