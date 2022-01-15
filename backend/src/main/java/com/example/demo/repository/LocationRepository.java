package com.example.demo.repository;

import com.example.demo.domain.Label;
import com.example.demo.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findByName(String name);
}
