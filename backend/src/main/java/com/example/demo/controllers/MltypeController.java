package com.example.demo.controllers;

import com.example.demo.domain.Mltype;
import com.example.demo.dto.MltypeDTO;
import com.example.demo.services.MltypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class MltypeController {

    @Autowired
    private MltypeService service;

    @GetMapping(value = "/Mltypes", produces = "application/json")
    public @ResponseBody
    MltypeDTO getMltypes() {
        List<Mltype> data = service.findAll();
        return new MltypeDTO(data);
    }
}
