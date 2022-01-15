package com.example.demo.repository;

import com.example.demo.domain.Dups;
import com.example.demo.domain.Stype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StypeRepository extends JpaRepository<Stype, Integer> {
    Stype findByName(String name);
}
