package com.example.demo.controllers;

import com.example.demo.domain.Outtable;
import com.example.demo.dto.OuttableDTO;
import com.example.demo.services.OuttableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class outtableController {

    @Autowired
    private OuttableService service;

    @GetMapping(value = "/Outtable", produces = "application/json")
    public @ResponseBody
    OuttableDTO getOuttable() {
        List<Outtable> data = service.findAll();
        return new OuttableDTO(data);
    }
}
