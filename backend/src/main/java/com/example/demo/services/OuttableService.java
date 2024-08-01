package com.example.demo.services;

import com.example.demo.domain.Outtable;
import com.example.demo.repository.OuttableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OuttableService {

    @Autowired
    private OuttableRepository repository;

    public List<Outtable> findAll() {

        return repository.findAll();
    }

}
