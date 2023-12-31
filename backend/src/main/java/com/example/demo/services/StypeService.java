package com.example.demo.services;

import com.example.demo.domain.Stype;
import com.example.demo.repository.StypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StypeService {

    @Autowired
    private StypeRepository repository;

    public List<Stype> findAll() {

        return repository.findAll();
    }

}
