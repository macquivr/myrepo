package com.example.demo.dto;

import com.example.demo.domain.*;
import java.util.List;

public class LtypesDTO {
    private List<Ltype> ltypes;

    public LtypesDTO(List<Ltype> dt) {
        ltypes = dt;
    }

    public List<Ltype> getLtypes() { return ltypes; }
    public void setLtypes(List<Ltype> d) { ltypes = d; }
}
