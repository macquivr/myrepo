package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;

    private String type;
    private LocalDate created;
    private LocalDate lastUsed;

    public String getName()
    {
        return name;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state")
    private Location state;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country")
    private Location country;

    public int getId() { return id; }

    public String getType() { return type; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
    public Location getState() { return state; }
    public Location getCountry() { return country; }

    public void setType(String t) { type = t; }
    public void setName(String n) { name = n; }
    public void setCreated(LocalDate d) { created = d; }
    public void setLastUsed(LocalDate d) { lastUsed = d; }
    public void setState(Location l) { state = l; }
    public void setCountry(Location c) { country = c; }
}
