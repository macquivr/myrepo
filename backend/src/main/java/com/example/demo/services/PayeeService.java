package com.example.demo.services;

import com.example.demo.domain.Payee;
import com.example.demo.repository.PayeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayeeService {

    @Autowired
    private PayeeRepository repository;

    public List<Payee> findAll() {

        return (List<Payee>) repository.findAll();
    }

}
