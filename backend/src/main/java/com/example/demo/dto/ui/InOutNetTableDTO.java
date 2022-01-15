package com.example.demo.dto.ui;

import java.util.List;

public class InOutNetTableDTO {
    private List<InOutNetRowDTO> Inoutnet;

    public InOutNetTableDTO() { /* nop */ }

    public InOutNetTableDTO(List<InOutNetRowDTO> dt) {
        Inoutnet = dt;
    }

    public List<InOutNetRowDTO> getInoutnet() { return Inoutnet; }
    public void setInoutnet(List<InOutNetRowDTO> d) { Inoutnet = d; }
}
