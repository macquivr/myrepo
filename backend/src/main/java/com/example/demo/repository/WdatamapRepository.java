package com.example.demo.repository;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.Wdatamap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WdatamapRepository extends JpaRepository<Wdatamap, Integer> {
    List<Wdatamap> findAllByWidOrderByWho(Payperiod pp);

}
