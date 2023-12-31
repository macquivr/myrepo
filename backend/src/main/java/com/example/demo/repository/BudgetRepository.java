package com.example.demo.repository;

import com.example.demo.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findAllByBdateBetweenOrderByBdateAsc(LocalDate start, LocalDate stop);
}
