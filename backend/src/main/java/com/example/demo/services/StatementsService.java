package com.example.demo.services;

import com.example.demo.domain.Statements;
import com.example.demo.repository.StatementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementsService {

    @Autowired
    private StatementsRepository repository;

    public List<Statements> findAll() {
        return repository.findAll();
    }

}
