package com.example.demo.controllers;

import com.example.demo.domain.TLedger;
import com.example.demo.domain.Wdatamap;
import com.example.demo.dto.*;
import com.example.demo.services.WdatamapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class WdatamapController {
    @Autowired
    private WdatamapService service;

    @GetMapping(value = "/Wdatamap", produces = "application/json")
    public @ResponseBody
    WdatamapDTO getWdatamap() {
        List<Wdatamap> data = service.findAll();
        return new WdatamapDTO(data);
    }

    @GetMapping(value = "/wdatamaptable", produces = "application/json")
    public @ResponseBody
    WdatamapTableDTO getWdatamapTable() {
        List<Wdatamap> data = service.findAll();
        List<WdatamapRowDTO> tdata = data.stream().map(WdatamapRowDTO::new).collect(Collectors.toList());

        return new WdatamapTableDTO(tdata);
    }
}
