package com.example.demo.controllers;

import com.example.demo.domain.Checks;
import com.example.demo.dto.ChecksDTO;
import com.example.demo.services.ChecksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ChecksController {

    @Autowired
    private ChecksService service;

    @GetMapping(value = "/checks", produces = "application/json")
    public @ResponseBody
    ChecksDTO getChecks() {
        List<Checks> data = service.findAll();
        return new ChecksDTO(data);
    }
}
