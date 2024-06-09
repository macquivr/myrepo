package com.example.demo.repository;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Ltype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecksRepository extends JpaRepository<Checks, Integer> {
    Checks findByLtypeAndCheckNum(Ltype ltype, int checkNum);
}
