package com.example.demo.bean;

import java.time.LocalDate;

public class StartStop {
    private LocalDate start;
    private LocalDate stop;

    public StartStop() { /* nop */ }
    public StartStop(LocalDate d1, LocalDate d2) {
        start = d1;
        stop = d2;
    }

    public void setStart(LocalDate start) { this.start = start; }
    public void setStop(LocalDate stop) { this.stop = stop; }

    public LocalDate getStart() { return start; }
    public LocalDate getStop() { return stop; }
}
