package com.example.demo.repository;

import com.example.demo.domain.Kvp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KvpRepository  extends JpaRepository<Kvp, Integer> {
    List<Kvp> findAllByType(int type);
    Kvp findByName(String name);
}

