package com.example.demo.services;

import com.example.demo.domain.Names;
import com.example.demo.repository.NamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamesService {

    @Autowired
    private NamesRepository repository;

    public List<Names> findAll() {

        return (List<Names>) repository.findAll();
    }

}
