package com.example.demo.controllers;

import com.example.demo.dto.StatusDTO;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping(value = "/report/{sessionId}", produces = "application/json")
    public @ResponseBody
    StatusDTO genReport(@PathVariable String sessionId) {
        return service.genReport(sessionId);
    }

}
