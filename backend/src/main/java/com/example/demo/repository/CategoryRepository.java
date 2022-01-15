package com.example.demo.repository;

import com.example.demo.domain.Category;
import com.example.demo.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
