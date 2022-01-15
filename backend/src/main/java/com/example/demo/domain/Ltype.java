package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Ltype {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private Boolean bundle;
    private String type;
    private LocalDate created;
    private LocalDate lastUsed;

    public int getId() { return id; }
    public String getName() { return name; }
    public Boolean getBundle() { return bundle; }
    public String getType() { return type; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
}
