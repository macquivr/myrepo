package com.example.demo.services;

import com.example.demo.domain.Wdatamap;
import com.example.demo.repository.WdatamapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WdatamapService {
    private static final Logger logger= LoggerFactory.getLogger(WdatamapService.class);

    @Autowired
    private WdatamapRepository repository;

    public List<Wdatamap> findAll() {
        return repository.findAll();
    }
}
