package com.example.demo.repository;

import com.example.demo.domain.Utilities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UtilitiesRepository extends JpaRepository<Utilities, Integer> {
    List<Utilities> findAllByDateBetween(LocalDate start, LocalDate stop);
    List<Utilities> findAllByDateBetweenOrderByDateAsc(LocalDate start, LocalDate stop);
}
