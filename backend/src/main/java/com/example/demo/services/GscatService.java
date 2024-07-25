package com.example.demo.services;

import com.example.demo.domain.Gscat;
import com.example.demo.repository.GscatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GscatService {

    @Autowired
    private GscatRepository repository;

    public List<Gscat> findAll() {

        return repository.findAll();
    }

}
