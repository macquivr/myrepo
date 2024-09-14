package com.example.demo.controllers;

import com.example.demo.domain.Mlt;
import com.example.demo.dto.MltRowDTO;
import com.example.demo.dto.MltTableDTO;
import com.example.demo.services.MltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MltController {

    @Autowired
    private MltService service;

    @GetMapping(value = "/mlttable", produces = "application/json")
    public @ResponseBody
    MltTableDTO getMlt() {
        List<Mlt> data = service.findAll();

        List<MltRowDTO> tdata = data.stream().map(MltRowDTO::new).collect(Collectors.toList());

        return new MltTableDTO(tdata);

    }
}
