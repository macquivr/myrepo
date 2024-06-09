package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Kvp {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int type;
    private String name;

    public int getId() { return this.id; }
    public int getType() { return this.type; }
    public String getName() { return this.name; }

    public void setType(int t) { this.type = t; }
    public void setName(String n) { this.name = n; }
}

