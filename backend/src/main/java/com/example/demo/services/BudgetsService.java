package com.example.demo.services;

import com.example.demo.domain.Budgets;
import com.example.demo.repository.BudgetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetsService {

    @Autowired
    private BudgetsRepository repository;

    public List<Budgets> findAll() {

        return repository.findAll();
    }

}
