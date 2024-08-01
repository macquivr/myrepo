package com.example.demo.controllers;

import com.example.demo.domain.Intable;
import com.example.demo.dto.IntableDTO;
import com.example.demo.services.IntableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class IntableController {

    @Autowired
    private IntableService service;

    @GetMapping(value = "/Intable", produces = "application/json")
    public @ResponseBody
    IntableDTO getIntable() {
        List<Intable> data = service.findAll();
        return new IntableDTO(data);
    }
}
