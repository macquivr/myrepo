package com.example.demo.services;

import com.example.demo.domain.Pptlm;
import com.example.demo.repository.PptlmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PptlmService {
    private static final Logger logger= LoggerFactory.getLogger(PptlmService.class);

    @Autowired
    private PptlmRepository repository;

    public List<Pptlm> findAll() {
        return repository.findAll();
    }
}
