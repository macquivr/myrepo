package com.example.demo.services;

import com.example.demo.domain.Mltype;
import com.example.demo.repository.MltypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MltypeService {

    @Autowired
    private MltypeRepository repository;

    public List<Mltype> findAll() {

        return repository.findAll();
    }

}
