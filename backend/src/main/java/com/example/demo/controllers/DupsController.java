package com.example.demo.controllers;

import com.example.demo.domain.Dups;
import com.example.demo.dto.DupsDTO;
import com.example.demo.services.DupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class DupsController {

    @Autowired
    private DupsService service;

    @GetMapping(value = "/Dups", produces = "application/json")
    public @ResponseBody
    DupsDTO getDups() {
        List<Dups> data = service.findAll();
        return new DupsDTO(data);
    }
}
