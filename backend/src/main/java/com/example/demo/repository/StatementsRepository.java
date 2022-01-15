package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StatementsRepository extends JpaRepository<Statements, Integer> {
    Statements findByName(String name);
    Statements findByStmtdate(LocalDate date);
    List<Statements> findAllByStmtdateBetween(LocalDate start, LocalDate stop);
}
