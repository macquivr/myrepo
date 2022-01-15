package com.example.demo.controllers;

import com.example.demo.dto.ui.UIKeyValueDTO;
import com.example.demo.services.UIHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UIHelperController {

    @Autowired
    private UIHelperService service;

    @GetMapping(value = "/payees", produces = "application/json")
    public @ResponseBody
    List<UIKeyValueDTO> getPayees() {
        return service.getPayees();
    }

    @GetMapping(value = "/stypes", produces = "application/json")
    public @ResponseBody
    List<UIKeyValueDTO> getStypes() {
        return service.getStypes();
    }

}
