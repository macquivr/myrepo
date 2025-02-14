package com.example.demo.repository;

import com.example.demo.domain.Gscat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GscatRepository extends JpaRepository<Gscat, Integer> {
    List<Gscat> findAllByCtype(Integer ctype);
}
