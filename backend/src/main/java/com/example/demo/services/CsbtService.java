package com.example.demo.services;

import com.example.demo.domain.Csbt;
import com.example.demo.repository.CsbtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsbtService {

    @Autowired
    private CsbtRepository repository;

    public List<Csbt> findAll() {

        return  repository.findAll();
    }

}
