package com.example.demo.repository;

import com.example.demo.domain.Gscat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GscatRepository extends JpaRepository<Gscat, Integer> {
    /* nop */
}
