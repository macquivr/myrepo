package com.example.demo.controllers;

import com.example.demo.domain.Checktype;
import com.example.demo.dto.CheckTypeDTO;
import com.example.demo.services.CheckTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class CheckTypeController {

    @Autowired
    private CheckTypeService service;

    @GetMapping(value = "/checktypes", produces = "application/json")
    public @ResponseBody
    CheckTypeDTO getCheckType() {
        List<Checktype> data = service.findAll();
        return new CheckTypeDTO(data);
    }
}
