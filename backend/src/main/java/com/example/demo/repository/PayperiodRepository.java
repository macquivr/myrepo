package com.example.demo.repository;

import com.example.demo.domain.Ledger;
import com.example.demo.domain.Payperiod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayperiodRepository extends JpaRepository<Payperiod, Integer> {
    List<Payperiod> findAllByStartBetweenOrderByStartAsc(LocalDate start, LocalDate stop);
}
