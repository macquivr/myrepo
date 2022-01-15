package com.example.demo.controllers;

import com.example.demo.domain.Statement;
import com.example.demo.dto.StatementRowDTO;
import com.example.demo.dto.StatementTableDTO;
import com.example.demo.dto.StatementDTO;
import com.example.demo.services.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class StatementController {

    @Autowired
    private StatementService service;

    @GetMapping(value = "/Statement", produces = "application/json")
    public @ResponseBody
    StatementDTO getStatement() {
        List<Statement> data = service.findAll();
        StatementDTO ret = new StatementDTO(data);

        return ret;
    }

    @GetMapping(value = "/statementable", produces = "application/json")
    public @ResponseBody
    StatementTableDTO getStatementTable() {
        List<Statement> data = service.findAll();

        List<StatementRowDTO> tdata = data.stream().map(StatementRowDTO::new).collect(Collectors.toList());

        return new StatementTableDTO(tdata);

    }
}
