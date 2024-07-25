package com.example.demo.services;

import com.example.demo.domain.Lmap;
import com.example.demo.repository.LmapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LmapService {

    @Autowired
    private LmapRepository repository;

    public List<Lmap> findAll() {

        return repository.findAll();
    }

}
