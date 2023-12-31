package com.example.demo.services;

import com.example.demo.domain.Checktype;
import com.example.demo.repository.CheckTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckTypeService {

    @Autowired
    private CheckTypeRepository repository;

    public List<Checktype> findAll() {

        return repository.findAll();
    }

}
