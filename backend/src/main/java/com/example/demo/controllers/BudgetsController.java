package com.example.demo.controllers;

import com.example.demo.domain.Budget;
import com.example.demo.domain.Budgets;
import com.example.demo.dto.BudgetRowDTO;
import com.example.demo.dto.BudgetTableDTO;
import com.example.demo.dto.BudgetsRowDTO;
import com.example.demo.dto.BudgetsTableDTO;
import com.example.demo.services.BudgetService;
import com.example.demo.services.BudgetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BudgetsController {

    @Autowired
    private BudgetsService service;

    @GetMapping(value = "/budgetstable", produces = "application/json")
    public @ResponseBody
    BudgetsTableDTO getBudgets() {
        List<Budgets> data = service.findAll();

        List<BudgetsRowDTO> tdata = data.stream().map(BudgetsRowDTO::new).collect(Collectors.toList());

        return new BudgetsTableDTO(tdata);

    }
}
