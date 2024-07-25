package com.example.demo.controllers;

import com.example.demo.domain.Inmap;
import com.example.demo.dto.InmapDTO;
import com.example.demo.services.InmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class InmapController {

    @Autowired
    private InmapService service;

    @GetMapping(value = "/Inmap", produces = "application/json")
    public @ResponseBody
    InmapDTO getInmap() {
        List<Inmap> data = service.findAll();
        return new InmapDTO(data);
    }
}
