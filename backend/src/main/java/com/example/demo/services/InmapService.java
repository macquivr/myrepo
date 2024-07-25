package com.example.demo.services;

import com.example.demo.domain.Inmap;
import com.example.demo.repository.InmapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmapService {

    @Autowired
    private InmapRepository repository;

    public List<Inmap> findAll() {

        return repository.findAll();
    }

}
