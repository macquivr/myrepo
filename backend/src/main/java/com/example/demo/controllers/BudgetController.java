package com.example.demo.controllers;

import com.example.demo.domain.Budget;
import com.example.demo.dto.BudgetTableDTO;
import com.example.demo.dto.BudgetRowDTO;
import com.example.demo.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BudgetController {

    @Autowired
    private BudgetService service;

    @GetMapping(value = "/budgettable", produces = "application/json")
    public @ResponseBody
    BudgetTableDTO getBudget() {
        List<Budget> data = service.findAll();

        List<BudgetRowDTO> tdata = data.stream().map(BudgetRowDTO::new).collect(Collectors.toList());

        return new BudgetTableDTO(tdata);

    }
}
