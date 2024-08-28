package com.example.demo.repository;

import com.example.demo.domain.Csbt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CsbtRepository extends JpaRepository<Csbt, Integer> {
    List<Csbt> findAllByDtBetweenOrderByDtAsc(LocalDate start, LocalDate stop);
}
