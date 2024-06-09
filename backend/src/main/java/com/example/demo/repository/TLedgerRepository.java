package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface TLedgerRepository extends JpaRepository<TLedger, Integer> {

    List<TLedger> findAllByLabelOrderByTdateAsc(Label label);
    List<TLedger> findAllByTdateBetweenOrderByTdateAsc(LocalDate start, LocalDate stop);
    List<TLedger> findAllByTdateBetweenAndLtypeOrderByTdateAsc(LocalDate start, LocalDate stop, Ltype ltype);
}
