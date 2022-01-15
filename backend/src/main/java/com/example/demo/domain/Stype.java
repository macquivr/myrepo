package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Stype {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDate created;
    private LocalDate lastUsed;

    public int getId() { return id; }
    public String getName()
    {
        return name;
    }
    public LocalDate getCreated()
    {
        return created;
    }
    public LocalDate getLastUsed()
    {
        return lastUsed;
    }
}
