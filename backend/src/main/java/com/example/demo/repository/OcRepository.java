package com.example.demo.repository;

import com.example.demo.domain.Oc;
import com.example.demo.domain.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OcRepository extends JpaRepository<Oc, Integer> {
    List<Oc> findAllByStmt(Statement stmt);
    List<Oc> findAllBySdate(LocalDate dt);
}
