package com.example.demo.dto;

import com.example.demo.domain.Ltype;

import java.time.LocalDate;

public class LtypeRowDTO {
    private String name;
    private String bundle;
    private String type;
    private LocalDate created;
    private LocalDate lastUsed;

    public LtypeRowDTO() { /* npo */ }
    public LtypeRowDTO(Ltype data) {
        name = data.getName();
        bundle = (data.getBundle()) ? "Yes" : "No";
        type = data.getType();
        created = data.getCreated();
        lastUsed = data.getLastUsed();
    }

    public void setName(String n) { name = n; }
    public void setBundle(String s) { bundle = s; }
    public void settype(String s) { type = s; }
    public void setCreated(LocalDate d) { created = d; }
    public void setLastUsed(LocalDate d) { lastUsed = d; }

    public String getName() { return name; }
    public String getBundle() { return bundle;}
    public String getType() { return type; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
}
