package com.example.demo.services;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.TLedger;
import com.example.demo.dto.SessionDTO;
import com.example.demo.repository.PayperiodRepository;
import com.example.demo.repository.TLedgerRepository;
import com.example.demo.state.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class PayperiodService {
    private static final Logger logger= LoggerFactory.getLogger(PayperiodService.class);

    @Autowired
    private PayperiodRepository repository;

    public List<Payperiod> findAll() {
        return repository.findAll();
    }
}


