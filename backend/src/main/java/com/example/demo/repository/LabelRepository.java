package com.example.demo.repository;

import com.example.demo.domain.Label;
import com.example.demo.domain.Statements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    Label findByName(String name);
    Label findById(int id);
}
