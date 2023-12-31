package com.example.demo.services;

import com.example.demo.domain.Checks;
import com.example.demo.repository.ChecksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecksService {

    @Autowired
    private ChecksRepository repository;

    public List<Checks> findAll() {

        return repository.findAll();
    }

}
