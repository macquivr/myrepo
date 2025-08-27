package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface LedgerRepository extends JpaRepository<Ledger, Integer> {

    List<Ledger> findAllByLtype(Ltype ltype);
    List<Ledger> findAllByStatement(Statement s);
    List<Ledger> findAllByStatementAndTransdateBetweenOrderByTransdateAsc(Statement s, LocalDate start, LocalDate stop);
    List<Ledger> findAllByLabelOrderByTransdateAsc(Label label);
    List<Ledger> findAllByTransdateBetweenOrderByTransdateAsc(LocalDate start, LocalDate stop);
    List<Ledger> findAllByTransdateBetweenAndAmountLessThanOrderByTransdateAsc(LocalDate start, LocalDate stop, double amt);
    List<Ledger> findAllByTransdateBetweenAndAmountGreaterThanOrderByTransdateAsc(LocalDate start, LocalDate stop, double amt);
    List<Ledger> findAllByTransdateBetweenAndLabelOrderByTransdateAsc(LocalDate start, LocalDate stop, Label lbl);
    List<Ledger> findAllByTransdateBetweenAndLtypeOrderByTransdateAsc(LocalDate start, LocalDate stop, Ltype ltype);
    List<Ledger> findAllByTransdateBetweenAndStypeOrderByTransdateAsc(LocalDate start, LocalDate stop, Stype stype);
    List<Ledger> findAllByTransdateBetweenAndLtypeAndStypeOrderByTransdateAsc(LocalDate start, LocalDate stop, Ltype ltype, Stype stype);
    List<Ledger> findAllByLabelOrderByTransdateDesc(Label lbl);
    List<Ledger> findAllByTransdateBeforeOrderByTransdateDesc(LocalDate start);
    List<Ledger> findAllByAmountAndTransdateBetweenOrderByTransdateAsc(double amount,LocalDate start, LocalDate stop);
}
