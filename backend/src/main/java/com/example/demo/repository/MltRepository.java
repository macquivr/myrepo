package com.example.demo.repository;

import com.example.demo.domain.Mlt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MltRepository extends JpaRepository<Mlt, Integer> {
    List<Mlt> findAllByDtBetweenOrderByDtAsc(LocalDate start, LocalDate stop);
}
