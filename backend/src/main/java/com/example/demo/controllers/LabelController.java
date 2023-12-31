package com.example.demo.controllers;

import com.example.demo.domain.Label;
import com.example.demo.dto.LabelDTO;
import com.example.demo.dto.JustNameDTO;
import com.example.demo.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class LabelController {

    @Autowired
    private LabelService service;

    @GetMapping(value = "/importLabels", produces = "application/json")
    public @ResponseBody
    JustNameDTO getImportLabels() {
        List<Label> data = service.findAll();
        return new JustNameDTO(data);
    }

    @GetMapping(value = "/Label", produces = "application/json")
    public @ResponseBody
    LabelDTO getLabel() {
        List<Label> data = service.findAll();
        return new LabelDTO(data);
    }
}
