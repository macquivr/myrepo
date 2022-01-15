package com.example.demo.controllers;

import com.example.demo.services.LtypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.dto.*;
import com.example.demo.domain.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class LtypeController {

    @Autowired
    private LtypeService service;

    @GetMapping(value = "/ltypes", produces = "application/json")
    public @ResponseBody
    LtypesDTO getLtypes() {
        List<Ltype> data = service.findAll();
        LtypesDTO ret = new LtypesDTO(data);

        return ret;
    }

    @GetMapping(value = "/ltypetable", produces = "application/json")
    public @ResponseBody
    LtypesTableDTO getLtypeTable() {
        List<Ltype> data = service.findAll();

        List<LtypeRowDTO> tdata = data.stream().map(LtypeRowDTO::new).collect(Collectors.toList());

        return new LtypesTableDTO(tdata);

    }
}
