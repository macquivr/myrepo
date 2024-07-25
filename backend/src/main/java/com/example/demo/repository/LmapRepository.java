package com.example.demo.repository;

import com.example.demo.domain.Lmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LmapRepository extends JpaRepository<Lmap, Integer> {
    Lmap findByLid(int id);
}
