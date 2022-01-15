package com.example.demo.repository;

import com.example.demo.domain.Label;
import com.example.demo.domain.Names;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NamesRepository extends JpaRepository<Names, Integer> {
    Names findByName(String name);
}
