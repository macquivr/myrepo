package com.example.demo.controllers;

import com.example.demo.dto.ImportDTO;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.dto.NameLocCatDTO;
import com.example.demo.services.ImportwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ImportControllerw {

    @Autowired
    private ImportwService service;

    /* main entry point */
    @GetMapping(value = "/importw/{sessionId}", produces = "application/json")
    public @ResponseBody
    ImportDTO importAction(@PathVariable String sessionId) {
        return service.importStatus(sessionId);
    }

    @PostMapping(value = "/importw/{sessionId}/update", produces = "application/json")
    public @ResponseBody
    StatusDTO updateImport(@PathVariable String sessionId, @RequestBody SessionUpdateDTO data) {
        return service.update(data);
    }

    @GetMapping(value = "/importw/{sessionId}/nameLocCat", produces = "application/json")
    public @ResponseBody
    NameLocCatDTO getNameLocCat(@PathVariable String sessionId) { return service.getNameLocCat(sessionId); }


    @GetMapping(value = "/importw/{sessionId}/nextdata", produces = "application/json")
    public @ResponseBody
    ImportDTO getNextCheck(@PathVariable String sessionId) {
        return service.getNextData(sessionId);
    }

    @GetMapping(value = "/importw/{sessionId}/nextlabel", produces = "application/json")
    public @ResponseBody
    ImportDTO getNextLabel(@PathVariable String sessionId) {
        return service.getNextLabel(sessionId);
    }
}
