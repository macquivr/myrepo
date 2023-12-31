package com.example.demo.services;

import com.example.demo.domain.Utilities;
import com.example.demo.repository.UtilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilitiesService {

    @Autowired
    private UtilitiesRepository repository;

    public List<Utilities> findAll() {

        return repository.findAll();
    }

}
