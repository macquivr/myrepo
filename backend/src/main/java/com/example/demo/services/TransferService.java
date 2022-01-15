package com.example.demo.services;

import com.example.demo.domain.Transfer;
import com.example.demo.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository repository;

    public List<Transfer> findAll() {

        return (List<Transfer>) repository.findAll();
    }

}
