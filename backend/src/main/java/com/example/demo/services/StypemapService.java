package com.example.demo.services;

import com.example.demo.domain.Stypemap;
import com.example.demo.repository.StypemapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StypemapService {

    @Autowired
    private StypemapRepository repository;

    public List<Stypemap> findAll() {

        return (List<Stypemap>) repository.findAll();
    }

}
