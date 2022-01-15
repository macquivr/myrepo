package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import com.example.demo.dto.ui.DatasourceDTO;
import com.example.demo.services.ChartService;

@RestController
@RequestMapping("/")
public class ChartController {
    private static final Logger logger=LoggerFactory.getLogger(ChartController.class);

    @Autowired
    private ChartService service;

    @GetMapping(value = "/chart/stype/atm/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypeAtm(@PathVariable String sessionId) {
       return service.getChartStypeAtm(sessionId);
    }

    @GetMapping(value = "/chart/stype/pos/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypePos(@PathVariable String sessionId) {
        return service.getChartStypePos(sessionId);
    }

    @GetMapping(value = "/chart/stype/misc/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypeMisc(@PathVariable String sessionId) {
        return service.getChartStypeMisc(sessionId);
    }

    @GetMapping(value = "/chart/stype/bills/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypeBills(@PathVariable String sessionId) {
        return service.getChartStypeBills(sessionId);
    }

    @GetMapping(value = "/chart/stype/annual/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypeAnnual(@PathVariable String sessionId) {
        return service.getChartStypeAnnual(sessionId);
    }

    @GetMapping(value = "/chart/stype/credit/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartStypeCredit(@PathVariable String sessionId) {
        return service.getChartStypeCredit(sessionId);
    }

    @GetMapping(value = "/chart/category/credit/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartCategoryCredit(@PathVariable String sessionId) {
        return service.getChartCategory(sessionId,false);
    }

    @GetMapping(value = "/chart/category/debit/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getChartCategoryDebit(@PathVariable String sessionId) {
        return service.getChartCategory(sessionId,true);
    }

}
