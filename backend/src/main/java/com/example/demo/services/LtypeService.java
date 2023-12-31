package com.example.demo.services;

import org.springframework.stereotype.Service;
import com.example.demo.domain.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class LtypeService {

    @Autowired
    private LtypeRepository repository;

    public List<Ltype> findAll() {

        return repository.findAll();
    }

}
