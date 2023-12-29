package com.example.demo.dto.ui;

import com.example.demo.dto.BudgetRowDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class BVNTableDTO extends BTable {
    private List<BVNRowDTO> Bvn;

    public BVNTableDTO() { /* nop */ }

    public List<BVNRowDTO> getBvn() { return this.Bvn; }
    public void setBvn(List<BVNRowDTO> d) { this.Bvn = d; }
}
