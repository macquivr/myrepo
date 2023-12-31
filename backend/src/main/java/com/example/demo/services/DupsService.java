package com.example.demo.services;

import com.example.demo.domain.Dups;
import com.example.demo.repository.DupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DupsService {

    @Autowired
    private DupsRepository repository;

    public List<Dups> findAll() {

        return repository.findAll();
    }

}
