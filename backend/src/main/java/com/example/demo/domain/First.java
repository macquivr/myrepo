package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class First {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private LocalDate aaa;
    private LocalDate amazon;
    private LocalDate annual;
    private LocalDate capitalone;
    private LocalDate citi;
    private LocalDate emmasave;
    private LocalDate main;
    private LocalDate mainsave;
    private LocalDate merrilynch;
    private LocalDate mortg;
    private LocalDate slush;
    private LocalDate usaa;

    public int getId() { return id; }

    public LocalDate getAaa()
    {
        return aaa;
    }
    public LocalDate getAmazon()
    {
        return amazon;
    }
    public LocalDate getAnnual()
    {
        return annual;
    }
    public LocalDate getCapitalone()
    {
        return capitalone;
    }
    public LocalDate getCiti()
    {
        return citi;
    }
    public LocalDate getEmmasave()
    {
        return emmasave;
    }
    public LocalDate getMain()
    {
        return main;
    }
    public LocalDate getMainsave()
    {
        return mainsave;
    }
    public LocalDate getMerrilynch()
    {
        return merrilynch;
    }
    public LocalDate getMortg()
    {
        return mortg;
    }
    public LocalDate getSlush()
    {
        return slush;
    }
    public LocalDate getUsaa()
    {
        return usaa;
    }
}
