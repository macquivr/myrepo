package com.example.demo.controllers;

import com.example.demo.domain.TLedger;
import com.example.demo.dto.TLedgerDTO;
import com.example.demo.dto.TLedgerRowDTO;
import com.example.demo.dto.TLedgerTableDTO;
import com.example.demo.services.TLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Vector;
import java.util.stream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/")
public class TLedgerController {
    private static final Logger logger=LoggerFactory.getLogger(TLedgerController.class);

    @Autowired
    private TLedgerService service;

    @GetMapping(value = "/TLedger", produces = "application/json")
    public @ResponseBody
    TLedgerDTO getTLedger() {
        List<TLedger> data = service.findAll();
        return new TLedgerDTO(data);
    }

    @GetMapping(value = "/tledgertable", produces = "application/json")
    public @ResponseBody
    TLedgerTableDTO getTLedgerTable() {
        List<TLedger> data = service.findAll();
        List<TLedgerRowDTO> tdata = data.stream().map(TLedgerRowDTO::new).collect(Collectors.toList());

        return new TLedgerTableDTO(tdata);
    }

    @GetMapping(value = "/tledger/{sessionId}", produces = "application/json")
    public @ResponseBody
    TLedgerTableDTO getTLedgerData(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new TLedgerTableDTO(new Vector<>());
        }
        List<TLedger> data = service.getData(sessionId);
        List<TLedgerRowDTO> tdata = data.stream().map(TLedgerRowDTO::new).collect(Collectors.toList());

        return new TLedgerTableDTO(tdata);
    }
}
