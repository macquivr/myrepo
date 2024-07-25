package com.example.demo.services;

import com.example.demo.domain.Cmap;
import com.example.demo.repository.CmapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmapService {

    @Autowired
    private CmapRepository repository;

    public List<Cmap> findAll() {

        return repository.findAll();
    }

}
