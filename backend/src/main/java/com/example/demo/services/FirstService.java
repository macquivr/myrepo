package com.example.demo.services;

import com.example.demo.domain.First;
import com.example.demo.repository.FirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirstService {

    @Autowired
    private FirstRepository repository;

    public List<First> findAll() {

        return  repository.findAll();
    }

}
