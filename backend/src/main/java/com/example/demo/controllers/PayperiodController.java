package com.example.demo.controllers;

import com.example.demo.domain.Payperiod;
import com.example.demo.domain.TLedger;
import com.example.demo.dto.PayperiodDTO;
import com.example.demo.dto.TLedgerDTO;
import com.example.demo.services.PayperiodService;
import com.example.demo.services.TLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class PayperiodController {

    @Autowired
    private PayperiodService service;

    @GetMapping(value = "/Payperiod", produces = "application/json")
    public @ResponseBody
    PayperiodDTO getPayperiod() {
        List<Payperiod> data = service.findAll();
        return new PayperiodDTO(data);
    }
}
