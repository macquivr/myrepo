package com.example.demo.controllers;

import com.example.demo.domain.Ledger;
import com.example.demo.dto.LedgerDTO;
import com.example.demo.dto.LedgerRowDTO;
import com.example.demo.dto.LedgerTableDTO;
import com.example.demo.services.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Vector;
import java.util.stream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/")
public class LedgerController {
    private static final Logger logger=LoggerFactory.getLogger(LedgerController.class);

    @Autowired
    private LedgerService service;

    @GetMapping(value = "/Ledger", produces = "application/json")
    public @ResponseBody
    LedgerDTO getLedger() {
        List<Ledger> data = service.findAll();
        LedgerDTO ret = new LedgerDTO(data);

        return ret;
    }

    @GetMapping(value = "/ledgertable", produces = "application/json")
    public @ResponseBody
    LedgerTableDTO getLedgerTable() {
        List<Ledger> data = service.findAll();
        List<LedgerRowDTO> tdata = data.stream().map(LedgerRowDTO::new).collect(Collectors.toList());

        return new LedgerTableDTO(tdata);
    }

    @GetMapping(value = "/ledger/{sessionId}", produces = "application/json")
    public @ResponseBody
    LedgerTableDTO getLedgerData(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new LedgerTableDTO(new Vector<LedgerRowDTO>());
        }
        List<Ledger> data = service.getData(sessionId);
        List<LedgerRowDTO> tdata = data.stream().map(LedgerRowDTO::new).collect(Collectors.toList());

        return new LedgerTableDTO(tdata);
    }
}
