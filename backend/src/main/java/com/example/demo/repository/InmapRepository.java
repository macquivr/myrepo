package com.example.demo.repository;

import com.example.demo.domain.Inmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InmapRepository extends JpaRepository<Inmap, Integer> {
    Inmap findByLid(int id);
}
