package com.example.demo.services;

import com.example.demo.domain.Statement;
import com.example.demo.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementService {

    @Autowired
    private StatementRepository repository;

    public List<Statement> findAll() {

        return repository.findAll();
    }

}
