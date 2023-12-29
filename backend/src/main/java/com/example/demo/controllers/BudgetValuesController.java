package com.example.demo.controllers;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgetvalues;
import com.example.demo.dto.BudgetRowDTO;
import com.example.demo.dto.BudgetTableDTO;
import com.example.demo.dto.BudgetValueRowDTO;
import com.example.demo.dto.BudgetValueTableDTO;
import com.example.demo.services.BudgetValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BudgetValuesController {

    @Autowired
    private BudgetValueService service;

    @GetMapping(value = "/budgetvaluetable", produces = "application/json")
    public @ResponseBody
    BudgetValueTableDTO getBudgetValue() {
        List<Budgetvalues> data = service.findAll();

        List<BudgetValueRowDTO> tdata = data.stream().map(BudgetValueRowDTO::new).collect(Collectors.toList());

        return new BudgetValueTableDTO(tdata);

    }
}
