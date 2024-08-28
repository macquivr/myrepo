package com.example.demo.controllers;

import com.example.demo.domain.Csbt;
import com.example.demo.dto.CsbtRowDTO;
import com.example.demo.dto.CsbtTableDTO;
import com.example.demo.services.CsbtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class CsbtController {

    @Autowired
    private CsbtService service;

    @GetMapping(value = "/csbttable", produces = "application/json")
    public @ResponseBody
    CsbtTableDTO getCsbt() {
        List<Csbt> data = service.findAll();

        List<CsbtRowDTO> tdata = data.stream().map(CsbtRowDTO::new).collect(Collectors.toList());

        return new CsbtTableDTO(tdata);

    }
}
