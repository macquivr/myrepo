package com.example.demo.repository;

import com.example.demo.domain.Intable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntableRepository extends JpaRepository<Intable, Integer> {
   /* nop */
}
