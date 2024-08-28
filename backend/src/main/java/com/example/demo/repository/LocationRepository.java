package com.example.demo.repository;

import com.example.demo.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findByName(String name);
    List<Location> findAllByType(String type);
}
