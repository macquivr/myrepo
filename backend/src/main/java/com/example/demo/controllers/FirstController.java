package com.example.demo.controllers;

import com.example.demo.domain.First;
import com.example.demo.dto.FirstDTO;
import com.example.demo.services.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class FirstController {

    @Autowired
    private FirstService service;

    @GetMapping(value = "/First", produces = "application/json")
    public @ResponseBody
    FirstDTO getFirst() {
        List<First> data = service.findAll();
        return new FirstDTO(data);
    }
}
