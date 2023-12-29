package com.example.demo.services;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Utilities;
import com.example.demo.repository.BudgetRepository;
import com.example.demo.repository.UtilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository repository;

    public List<Budget> findAll() {

        return (List<Budget>) repository.findAll();
    }

}
