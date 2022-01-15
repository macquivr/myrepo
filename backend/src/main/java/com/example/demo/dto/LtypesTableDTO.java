package com.example.demo.dto;


import java.util.List;

public class LtypesTableDTO {
    private List<LtypeRowDTO> Ltype;

    public LtypesTableDTO(List<LtypeRowDTO> dt) {
        Ltype = dt;
    }

    public List<LtypeRowDTO> getLtype() { return Ltype; }
    public void setLtype(List<LtypeRowDTO> d) { Ltype = d; }
}
