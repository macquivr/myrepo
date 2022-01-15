package com.example.demo.services;

import com.example.demo.domain.Label;
import com.example.demo.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository repository;

    public List<Label> findAll() {

        return (List<Label>) repository.findAll();
    }

}
