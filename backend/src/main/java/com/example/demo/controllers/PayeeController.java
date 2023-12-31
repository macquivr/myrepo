package com.example.demo.controllers;

import com.example.demo.domain.Payee;
import com.example.demo.dto.PayeeDTO;
import com.example.demo.services.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class PayeeController {

    @Autowired
    private PayeeService service;

    @GetMapping(value = "/Payee", produces = "application/json")
    public @ResponseBody
    PayeeDTO getPayee() {
        List<Payee> data = service.findAll();
        return new PayeeDTO(data);
    }
}
