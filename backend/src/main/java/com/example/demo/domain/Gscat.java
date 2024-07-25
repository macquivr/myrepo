package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Gscat {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private int ctype;

    public int getId() { return id; }
    public String getName()
    {
        return this.name;
    }
    public int getCtype() { return this.ctype; }

    public void setName(String n) { name = n; }
    public void setCtype(int ctype) { this.ctype = ctype; }
}
