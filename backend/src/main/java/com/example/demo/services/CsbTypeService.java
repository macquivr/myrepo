package com.example.demo.services;

import com.example.demo.domain.Csbtype;
import com.example.demo.repository.CsbTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsbTypeService {

    @Autowired
    private CsbTypeRepository repository;

    public List<Csbtype> findAll() {

        return (List<Csbtype>) repository.findAll();
    }

}
