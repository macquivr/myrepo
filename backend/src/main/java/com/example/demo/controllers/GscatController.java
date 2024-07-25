package com.example.demo.controllers;

import com.example.demo.domain.Gscat;
import com.example.demo.dto.GscatDTO;
import com.example.demo.services.GscatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class GscatController {

    @Autowired
    private GscatService service;

    @GetMapping(value = "/Gscat", produces = "application/json")
    public @ResponseBody
    GscatDTO getGscat() {
        List<Gscat> data = service.findAll();
        return new GscatDTO(data);
    }
}
