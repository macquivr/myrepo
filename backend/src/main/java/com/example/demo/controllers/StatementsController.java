package com.example.demo.controllers;

import com.example.demo.domain.Statements;
import com.example.demo.dto.StatementsDTO;
import com.example.demo.services.StatementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class StatementsController {

    @Autowired
    private StatementsService service;

    @GetMapping(value = "/Statements", produces = "application/json")
    public @ResponseBody
    StatementsDTO getStatements() {
        List<Statements> data = service.findAll();
        StatementsDTO ret = new StatementsDTO(data);

        return ret;
    }
}
