package com.example.demo.controllers;

import com.example.demo.domain.Cmap;
import com.example.demo.dto.CmapDTO;
import com.example.demo.services.CmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class CmapController {

    @Autowired
    private CmapService service;

    @GetMapping(value = "/Cmap", produces = "application/json")
    public @ResponseBody
    CmapDTO getCmap() {
        List<Cmap> data = service.findAll();
        return new CmapDTO(data);
    }
}
