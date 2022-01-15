package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Payee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;

    private LocalDate created;
    private LocalDate lastUsed;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checktype")
    private Checktype checkType;

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
    public Checktype getCheckType() { return checkType; }
}
