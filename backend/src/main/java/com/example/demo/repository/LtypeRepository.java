package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.*;

public interface LtypeRepository extends JpaRepository<Ltype, Integer> {
    Ltype findByName(String name);
}
