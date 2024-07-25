package com.example.demo.controllers;

import com.example.demo.domain.Lmap;
import com.example.demo.dto.LmapDTO;
import com.example.demo.services.LmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class LmapController {

    @Autowired
    private LmapService service;

    @GetMapping(value = "/Lmap", produces = "application/json")
    public @ResponseBody
    LmapDTO getLmap() {
        List<Lmap> data = service.findAll();
        return new LmapDTO(data);
    }
}
