package com.example.demo.dto;

import com.example.demo.domain.Dups;

import java.util.List;

public class DupsDTO {
    private List<Dups> Dups;

    public DupsDTO(List<Dups> dt) {
        Dups = dt;
    }

    public List<Dups> getDups() { return Dups; }
    public void setDups(List<Dups> d) { Dups = d; }
}
