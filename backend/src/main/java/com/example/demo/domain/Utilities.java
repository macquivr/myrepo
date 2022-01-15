package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Utilities {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private Double cell;
    private Double electric;
    private Double cable;
    private LocalDate date;
    private String dstr;

    public int getId() { return id; }
    public Double getCell() { return cell; }
    public Double getElectric() { return electric; }
    public Double getCable() { return cable; }
    public LocalDate getDate() { return date; }
    public String getDstr() { return dstr; }

    public void setCell(Double d) {  cell = d; }
    public void setElectric(Double d) { electric = d; }
    public void setCable(Double d) { cable = d; }
    public void setDate(LocalDate d) { date = d; }
    public void setDstr(String d) { dstr = d; }
}
