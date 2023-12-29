package com.example.demo.repository;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgetvalues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetValuesRepository extends JpaRepository<Budgetvalues, Integer> {
   Budgetvalues findByName(String name);
}
