package com.example.demo.dto.ui;

import java.util.List;
import com.example.demo.state.TableDTOBase;
import java.io.*;

public class StypeTableDTO extends TableDTOBase {
    private List<StypeRowDTO> stype;

    public StypeTableDTO() { /* nop */ }

    public StypeTableDTO(List<StypeRowDTO> dt) {
        stype = dt;
    }

    public List<StypeRowDTO> getStype() { return stype; }
    public void setStype(List<StypeRowDTO> d) { stype = d; }

    public void doPercent() { this.percent(stype); }
    public void print() {
        FileWriter w = null;
        try {
            w = new FileWriter("results.csv");
            for (StypeRowDTO d : stype) {
                String str = d.toString();
                w.write(str);
                w.flush();
            }
            w.close();
        } catch (Exception ex) {
            return;
        }
    }
}
