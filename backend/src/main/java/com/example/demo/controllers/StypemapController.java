package com.example.demo.controllers;

import com.example.demo.domain.Stypemap;
import com.example.demo.dto.StypemapRowDTO;
import com.example.demo.dto.StypemapTableDTO;
import com.example.demo.dto.StypemapDTO;
import com.example.demo.services.StypemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class StypemapController {

    @Autowired
    private StypemapService service;

    @GetMapping(value = "/Stypemap", produces = "application/json")
    public @ResponseBody
    StypemapDTO getStypemap() {
        List<Stypemap> data = service.findAll();
        StypemapDTO ret = new StypemapDTO(data);

        return ret;
    }

    @GetMapping(value = "/stypemaptable", produces = "application/json")
    public @ResponseBody
    StypemapTableDTO getStypemapTable() {
        List<Stypemap> data = service.findAll();

        List<StypemapRowDTO> tdata = data.stream().map(StypemapRowDTO::new).collect(Collectors.toList());

        return new StypemapTableDTO(tdata);
    }
}
