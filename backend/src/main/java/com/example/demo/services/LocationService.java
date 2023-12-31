package com.example.demo.services;

import com.example.demo.domain.Location;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    public List<Location> findAll() {

        return repository.findAll();
    }

}
