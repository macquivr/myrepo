package com.example.demo.controllers;

import com.example.demo.dto.ui.DatasourceMsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ui.DatasourceDTO;
import com.example.demo.services.ChartService;

@RestController
@RequestMapping("/")
public class ChartController {
    private static final Logger logger=LoggerFactory.getLogger(ChartController.class);

    @Autowired
    private ChartService service;

    @GetMapping(value = "/chart/msline/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceMsDTO getMsline(@PathVariable String sessionId) {
        return service.getMsline(sessionId);
    }

    @GetMapping(value = "/chart/electric/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getElectric(@PathVariable String sessionId) {
        return service.getElectric(sessionId);
    }

    @GetMapping(value = "/chart/cell/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCell(@PathVariable String sessionId) {
        return service.getCell(sessionId);
    }

    @GetMapping(value = "/chart/cable/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCable(@PathVariable String sessionId) {
        return service.getCable(sessionId);
    }

    @GetMapping(value = "/chart/out/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getOut(@PathVariable String sessionId) {
        return service.getOut(sessionId);
    }

    @GetMapping(value = "/chart/in/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getIn(@PathVariable String sessionId) {
        return service.getIn(sessionId);
    }

    @GetMapping(value = "/chart/inoutnet/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getInOutNet(@PathVariable String sessionId) {
        return service.getInOutNet(sessionId);
    }

    @GetMapping(value = "/chart/netbudget/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getNetBudget(@PathVariable String sessionId) {
        return service.getNetBudget(sessionId);
    }


    @GetMapping(value = "/chart/ml/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getMl(@PathVariable String sessionId) {
        return service.getMl(sessionId);
    }

    @GetMapping(value = "/chart/busaa/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBusaa(@PathVariable String sessionId) {
        return service.getBusaa(sessionId);
    }

    @GetMapping(value = "/chart/baaa/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBaaa(@PathVariable String sessionId) {
        return service.getBaaa(sessionId);
    }

    @GetMapping(value = "/chart/bmortgage/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBmortgage(@PathVariable String sessionId) {
        return service.getBmortgage(sessionId);
    }

    @GetMapping(value = "/chart/bmedical/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBmedical(@PathVariable String sessionId) {
        return service.getBmedical(sessionId);
    }

    @GetMapping(value = "/chart/butilities/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getButilities(@PathVariable String sessionId) {
        return service.getButilities(sessionId);
    }

    @GetMapping(value = "/chart/bsears/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBsears(@PathVariable String sessionId) {
        return service.getBsears(sessionId);
    }

    @GetMapping(value = "/chart/bcapone/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBcapone(@PathVariable String sessionId) {
        return service.getBcapone(sessionId);
    }

    @GetMapping(value = "/chart/bamazon/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBamazon(@PathVariable String sessionId) {
        return service.getBamazon(sessionId);
    }

    @GetMapping(value = "/chart/bcredit/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBall(@PathVariable String sessionId) {
        return service.getBall(sessionId);
    }

    @GetMapping(value = "/chart/usaa/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getUsaa(@PathVariable String sessionId) {
        return service.getUsaa(sessionId);
    }

    @GetMapping(value = "/chart/credit/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCredit(@PathVariable String sessionId) {
        return service.getCredit(sessionId);
    }

    @GetMapping(value = "/chart/misc/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getMisc(@PathVariable String sessionId) {
        return service.getMisc(sessionId);
    }

    @GetMapping(value = "/chart/budget/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getBudget(@PathVariable String sessionId) {
        return service.getBudget(sessionId);
    }

    @GetMapping(value = "/chart/capone/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCapone(@PathVariable String sessionId) {
        return service.getCapone(sessionId);
    }

    @GetMapping(value = "/chart/caponefilter/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCaponeFiltered(@PathVariable String sessionId) {
        return service.getCaponeFiltered(sessionId);
    }

    @GetMapping(value = "/chart/caponeoff/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getCaponeOff(@PathVariable String sessionId) {
        return service.getCaponeOff(sessionId);
    }

    @GetMapping(value = "/chart/amazon/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getAmazon(@PathVariable String sessionId) {
        return service.getAmazon(sessionId);
    }

    @GetMapping(value = "/chart/aaa/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getAaa(@PathVariable String sessionId) {
        return service.getAaa(sessionId);
    }

    @GetMapping(value = "/chart/aaaoff/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getAaaOff(@PathVariable String sessionId) {
        return service.getAaaOff(sessionId);
    }



    @GetMapping(value = "/chart/pos/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getPos(@PathVariable String sessionId) {
        return service.getPOS(sessionId);
    }

    @GetMapping(value = "/chart/atm/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getAtm(@PathVariable String sessionId) {
        return service.getATM(sessionId);
    }

    @GetMapping(value = "/chart/dog/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getDog(@PathVariable String sessionId) {
        return service.getDog(sessionId);
    }


    @GetMapping(value = "/chart/utilities/{sessionId}", produces = "application/json")
    public @ResponseBody
    DatasourceDTO getUtilities(@PathVariable String sessionId) {
        return service.getUtilities(sessionId);
    }

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
