package com.example.demo.controllers;

import com.example.demo.domain.Stype;
import com.example.demo.dto.StypeDTO;
import com.example.demo.services.StypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class StypeController {

    @Autowired
    private StypeService service;

    @GetMapping(value = "/Stypes", produces = "application/json")
    public @ResponseBody
    StypeDTO getStype() {
        List<Stype> data = service.findAll();
        return new StypeDTO(data);
    }
}
