package com.example.demo.dto.ui;

import java.util.List;

public class BVNTableDTO extends BTable {
    private List<BVNRowDTO> Bvn;

    public BVNTableDTO() { /* nop */ }

    public List<BVNRowDTO> getBvn() { return this.Bvn; }
    public void setBvn(List<BVNRowDTO> d) { this.Bvn = d; }
}
