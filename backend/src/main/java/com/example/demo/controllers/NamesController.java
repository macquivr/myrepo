package com.example.demo.controllers;

import com.example.demo.domain.Names;
import com.example.demo.dto.JustNameDTO;
import com.example.demo.dto.NamesDTO;
import com.example.demo.services.NamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class NamesController {

    @Autowired
    private NamesService service;

    @GetMapping(value = "/importNames", produces = "application/json")
    public @ResponseBody
    JustNameDTO getImportNames() {
        List<Names> data = service.findAll();
        return new JustNameDTO(data);
    }

    @GetMapping(value = "/Names", produces = "application/json")
    public @ResponseBody
    NamesDTO getNames() {
        List<Names> data = service.findAll();
        return new NamesDTO(data);
    }
}
