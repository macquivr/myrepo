package com.example.demo.controllers;

import com.example.demo.dto.SessionDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.SessionService;

@RestController
@RequestMapping("/")
public class SessionController {

    @Autowired
    private SessionService service;

    @PostMapping(value = "/usession", produces = "application/json")
    public @ResponseBody
    StatusDTO updateSession(@RequestBody SessionUpdateDTO data) {
        return service.update(data);

    }

    @GetMapping(value = "/msession", produces = "application/json")
    public @ResponseBody
    SessionDTO newSession() {
        return service.newSession();
    }

    @GetMapping(value = "/gsession/{sessionId}", produces = "application/json")
    public @ResponseBody
    SessionDTO getSession(@PathVariable String sessionId) {
        return service.getSession(sessionId);
    }


}
