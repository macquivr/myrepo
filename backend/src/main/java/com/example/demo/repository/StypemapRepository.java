package com.example.demo.repository;

import com.example.demo.domain.Csbtype;
import com.example.demo.domain.Dups;
import com.example.demo.domain.Label;
import com.example.demo.domain.Stypemap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StypemapRepository extends JpaRepository<Stypemap, Integer> {
    Stypemap findByLabel(Label label);
    Stypemap findByCsbtype(Csbtype type);
}
