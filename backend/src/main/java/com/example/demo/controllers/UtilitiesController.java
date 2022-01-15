package com.example.demo.controllers;

import com.example.demo.domain.Utilities;
import com.example.demo.dto.UtilitiesDTO;
import com.example.demo.services.UtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class UtilitiesController {

    @Autowired
    private UtilitiesService service;

    @GetMapping(value = "/Utilities", produces = "application/json")
    public @ResponseBody
    UtilitiesDTO getUtilities() {
        List<Utilities> data = service.findAll();
        UtilitiesDTO ret = new UtilitiesDTO(data);

        return ret;
    }
}
