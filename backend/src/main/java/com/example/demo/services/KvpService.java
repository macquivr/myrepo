package com.example.demo.services;

import com.example.demo.domain.Kvp;
import com.example.demo.domain.Payperiod;
import com.example.demo.repository.KvpRepository;
import com.example.demo.repository.PayperiodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KvpService {
    private static final Logger logger= LoggerFactory.getLogger(KvpService.class);

    @Autowired
    private KvpRepository repository;

    public List<Kvp> findAll() {
        return repository.findAll();
    }
}
