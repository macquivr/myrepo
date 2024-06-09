package com.example.demo.controllers;

import com.example.demo.domain.Kvp;
import com.example.demo.dto.KvpDTO;
import com.example.demo.services.KvpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class KvpController {
    @Autowired
    private KvpService service;

    @GetMapping(value = "/Kvp", produces = "application/json")
    public @ResponseBody
    KvpDTO getKvp() {
        List<Kvp> data = service.findAll();
        return new KvpDTO(data);
    }
}
