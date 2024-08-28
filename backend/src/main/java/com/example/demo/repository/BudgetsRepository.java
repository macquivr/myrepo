package com.example.demo.repository;

import com.example.demo.domain.Budgets;
import com.example.demo.domain.Budgetvalues;
import com.example.demo.domain.Statements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetsRepository extends JpaRepository<Budgets, Integer> {
    List<Budgets> findAllByBdateBetweenOrderByBdateAsc(LocalDate start, LocalDate stop);
    List<Budgets> findAllByBdateBetweenAndBidOrderByBdateAsc(LocalDate start, LocalDate stop, Budgetvalues bv);
    List<Budgets> findAllByStmts(Statements stmt);
    Budgets findByStmtsAndBid(Statements stmt, Budgetvalues bv);
}
