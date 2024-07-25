package com.example.demo.repository;

import com.example.demo.domain.Cmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CmapRepository extends JpaRepository<Cmap, Integer> {
    Cmap findByCid(int id);
}
