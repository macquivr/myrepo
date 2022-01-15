package com.example.demo.repository;

import com.example.demo.domain.Dups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DupsRepository extends JpaRepository<Dups, Integer> {
    Dups findByDupLabel(String label);
}
