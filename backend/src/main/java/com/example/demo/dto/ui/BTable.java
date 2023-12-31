package com.example.demo.dto.ui;

import com.example.demo.dto.BudgetRowDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class BTable {
    protected HashMap<Integer, List<BudgetRowDTO>> makeBlist(List<BudgetRowDTO> data) {
        HashMap<Integer,List<BudgetRowDTO>> ret = new HashMap<>();

        for (BudgetRowDTO b : data) {
            Integer stmtsI = b.getStmts();
            List<BudgetRowDTO> bl = ret.get(stmtsI);
            if (bl == null) {
                bl = new Vector<>();
                bl.add(b);
                ret.put(stmtsI,bl);
            } else {
                bl.add(b);
            }
        }
        return ret;
    }
}
