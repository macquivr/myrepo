package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Category {
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

    public void setName(String n) { name = n; }
    public void setCreated(LocalDate d) { created = d; }
    public void setLastUsed(LocalDate d) { lastUsed = d; }
}
