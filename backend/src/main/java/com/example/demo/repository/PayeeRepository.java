package com.example.demo.repository;

import com.example.demo.domain.Payee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayeeRepository extends JpaRepository<Payee, Integer> {

}
