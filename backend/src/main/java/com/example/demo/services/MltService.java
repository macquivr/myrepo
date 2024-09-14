package com.example.demo.services;

import com.example.demo.domain.Mlt;
import com.example.demo.repository.MltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MltService {

    @Autowired
    private MltRepository repository;

    public List<Mlt> findAll() {

        return  repository.findAll();
    }

}
