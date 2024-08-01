package com.example.demo.services;

import com.example.demo.domain.Intable;
import com.example.demo.repository.IntableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntableService {

    @Autowired
    private IntableRepository repository;

    public List<Intable> findAll() {

        return repository.findAll();
    }

}
