package com.example.demo.services;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgetvalues;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.BudgetValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetValueService {

    @Autowired
    private BudgetValuesRepository repository;

    public List<Budgetvalues> findAll() {

        return (List<Budgetvalues>) repository.findAll();
    }

}
