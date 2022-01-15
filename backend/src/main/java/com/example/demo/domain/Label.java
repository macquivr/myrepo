package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDate created;
    private LocalDate lastUsed;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "names")
    private Names names;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    private Location location;

    public int getId() { return id; }

    public String getName() { return name; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
    public Location getLocation() { return location; }
    public Names getNames() { return names; }
    public Category getCategory() { return category; }

    public void setName(String n) { name = n; }
    public void setCreated(LocalDate d) { created = d; }
    public void setLastUsed(LocalDate d) { lastUsed = d; }
    public void setNames(Names n) { names = n; }
    public void setLocation(Location l) { location = l; }
    public void setCategory(Category c) { category = c; }
}
