package com.example.demo.repository;

import com.example.demo.domain.Budgets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetsRepository extends JpaRepository<Budgets, Integer> {
    List<Budgets> findAllByBdateBetweenOrderByBdateAsc(LocalDate start, LocalDate stop);
}
