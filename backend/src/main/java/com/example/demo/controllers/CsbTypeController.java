package com.example.demo.controllers;

import com.example.demo.domain.Csbtype;
import com.example.demo.dto.CsbTypeDTO;
import com.example.demo.services.CsbTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class CsbTypeController {

    @Autowired
    private CsbTypeService service;

    @GetMapping(value = "/CsbTypes", produces = "application/json")
    public @ResponseBody
    CsbTypeDTO getCsbType() {
        List<Csbtype> data = service.findAll();
        return new CsbTypeDTO(data);
    }
}
