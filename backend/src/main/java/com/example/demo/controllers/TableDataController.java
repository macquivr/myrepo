package com.example.demo.controllers;

import com.example.demo.dto.ui.*;
import com.example.demo.dto.*;
import com.example.demo.services.TableDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Vector;

@RestController
@RequestMapping("/")
public class TableDataController {
    private static final Logger logger= LoggerFactory.getLogger(TableDataController.class);

    @Autowired
    private TableDataService service;

    @GetMapping(value = "/inoutnet/{sessionId}", produces = "application/json")
    public @ResponseBody
    InOutNetTableDTO getInOutNet(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new InOutNetTableDTO(new Vector<InOutNetRowDTO>());
        }

        return service.doInOutNet(sessionId);
    }

    @GetMapping(value = "/bvs/{sessionId}", produces = "application/json")
    public @ResponseBody
    BVNTableDTO getBvs(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return null; // new InOutNetTableDTO(new Vector<InOutNetRowDTO>());
        }

        return service.doBvs(sessionId);
    }

    @GetMapping(value = "/bvalues/{sessionId}", produces = "application/json")
    public @ResponseBody
    BVNTableDTO getBvalues(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return null; // new InOutNetTableDTO(new Vector<InOutNetRowDTO>());
        }

        return service.doBvalues(sessionId);
    }

    @GetMapping(value = "/bnet/{sessionId}", produces = "application/json")
    public @ResponseBody
    BVNTableDTO getBnet(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return null;
        }

        return service.doBnet(sessionId);
    }

    @GetMapping(value = "/bsnet/{sessionId}", produces = "application/json")
    public @ResponseBody
    BVNTableDTO getBsnet(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return null;
        }

        return service.doBsnet(sessionId);
    }

    @GetMapping(value = "/balance/{sessionId}", produces = "application/json")
    public @ResponseBody
    BalanceTableDTO getBalances(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new BalanceTableDTO(new Vector<BalanceRowDTO>());
        }

        return service.doBalance(sessionId);
    }

    @GetMapping(value = "/cstatus/{sessionId}", produces = "application/json")
    public @ResponseBody
    CStatusTableDTO getCstatus(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new CStatusTableDTO();
        }

        return service.doCStatus(sessionId);
    }

    @GetMapping(value = "/stype/{sessionId}", produces = "application/json")
    public @ResponseBody
    StypeTableDTO getStype(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new StypeTableDTO(new Vector<StypeRowDTO>());
        }

        return service.doStype(sessionId);
    }

    @GetMapping(value = "/statements/{sessionId}", produces = "application/json")
    public @ResponseBody
    StatementTableDTO getStatements(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new StatementTableDTO(new Vector<StatementRowDTO>());
        }

        return service.doStatements(sessionId);
    }

    @GetMapping(value = "/bills/{sessionId}", produces = "application/json")
    public @ResponseBody
    BillsTableDTO getBills(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new BillsTableDTO(new Vector<BillsRowDTO>());
        }

        return service.doBills(sessionId);
    }

    @GetMapping(value = "/annual/{sessionId}", produces = "application/json")
    public @ResponseBody
    AnnualTableDTO getAnnual(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new AnnualTableDTO(new Vector<AnnualRowDTO>());
        }

        return service.doAnnual(sessionId);
    }

    @GetMapping(value = "/utils/{sessionId}", produces = "application/json")
    public @ResponseBody
    UtilsTableDTO getUtils(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new UtilsTableDTO(new Vector<UtilsRowDTO>());
        }

        return service.doUtils(sessionId);
    }

    @GetMapping(value = "/creditp/{sessionId}", produces = "application/json")
    public @ResponseBody
    CreditTableDTO getCreditp(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new CreditTableDTO(new Vector<CreditRowDTO>());
        }

        return service.doCreditp(sessionId);
    }

    @GetMapping(value = "/credits/{sessionId}", produces = "application/json")
    public @ResponseBody
    CreditTableDTO getCredits(@PathVariable String sessionId)
    {
        if ((sessionId != null) && sessionId.equals("null")) {
            logger.error("Bad session....");
            return new CreditTableDTO(new Vector<CreditRowDTO>());
        }

        return service.doCredits(sessionId);
    }

    @GetMapping(value = "/nlc/{sessionId}", produces = "application/json")
    public @ResponseBody
    NameLocCatDTO getNameLocCat(@PathVariable String sessionId) { return service.getNameLocCat(sessionId); }

    @GetMapping(value = "/categories/{sessionId}", produces = "application/json")
    public @ResponseBody
    CategoriesUIDTO getCategories(@PathVariable String sessionId) { return service.getCategoriesData(sessionId); }
}
