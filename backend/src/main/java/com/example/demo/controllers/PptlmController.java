package com.example.demo.controllers;

import com.example.demo.domain.Pptlm;
import com.example.demo.dto.PptlmDTO;
import com.example.demo.services.PptlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class PptlmController {
    @Autowired
    private PptlmService service;

    @GetMapping(value = "/Pptlm", produces = "application/json")
    public @ResponseBody
    PptlmDTO getPptlm() {
        List<Pptlm> data = service.findAll();
        return new PptlmDTO(data);
    }
}
