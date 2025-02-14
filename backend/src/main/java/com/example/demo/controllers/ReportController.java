package com.example.demo.controllers;
import com.example.demo.dto.StatusDTO;
import com.example.demo.dto.RValueDTO;
import com.example.demo.bean.RValue;
import com.example.demo.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReportController {
    public static boolean action = false;

    public static void flipAction() {
        action = !action;
    }

    public static boolean isAction() {
        return action;
    }
    @Autowired
    private ReportService service;

    @GetMapping(value = "/reports", produces = "application/json")
    public @ResponseBody
    RValueDTO getReport() {
        List<RValue> data = populate();
        return new RValueDTO(data);
    }

    @GetMapping(value = "/report/{sessionId}", produces = "application/json")
    public @ResponseBody
    StatusDTO genReport(@PathVariable String sessionId) {
        return service.genReport(sessionId);
    }

    private List<RValue> populate() {
        List<RValue> ret = new ArrayList<RValue>();


        if (ReportController.action) {
            actions(ret);
        } else {
            reports(ret);
        }

        return ret;
    }

    private void add(String v, String l, List<RValue> r) {
        RValue ret = new RValue();
        ret.setValue(v);
        ret.setLabel(l);
        r.add(ret);
    }

    private void actions(List<RValue> r) {
        add("PSTUFF", "Pstuff", r);
        add("PPTLM", "Pptlm", r);
        add("UPDATELABEL", "UpdateLastUsed", r);
        add("BUDGETSET", "BudgetSet", r);
        add("MLBALANCES", "MlBalances", r);
    }

    private void reports(List<RValue> r) {
        add("DEFAULT", "DEFAULT", r);
        add("NEWBUDGET", "NewBudget", r);
        add("NEWBALANCE", "NewBalance", r);
        add("MAIN",  "MainOnly" , r);
        add("RETIRE",  "Retire" , r);
        add("CREDITCAT",  "CreditCat" , r);
        add("INOUT",  "InOut" , r);
        add("IN",  "In" , r);
        add("OUT",  "Out" , r);
        add("MISC",  "Misc", r);
        add("BREPORT",  "BReport" , r);
        add("CREPORT",  "CReport" , r);
        add("OTHER",  "Other" , r);
        add("GREPORT",  "Greport" , r);
        add("PAYPERIOD",  "Payperiod" , r);
        add("SUMMARY",  "Summary",r);
        add("CATEGORY",  "Category",r);
     }
}
