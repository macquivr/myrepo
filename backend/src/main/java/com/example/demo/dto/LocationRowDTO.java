package com.example.demo.dto;

import com.example.demo.domain.Location;

import java.time.LocalDate;

public class LocationRowDTO {
    private String name;
    private String state;
    private String country;
    private String type;
    private LocalDate created;
    private LocalDate lastUsed;

    public LocationRowDTO() { /* npo */ }
    public LocationRowDTO(Location data) {
        name = data.getName();
        state = (data.getState() != null) ? data.getState().getName() : "N/A";
        country = (data.getCountry() != null) ? data.getCountry().getName() : "N/A";
        type = data.getType();
        created = data.getCreated();
        lastUsed = data.getLastUsed();
    }

    public void setName(String n) { name = n; }
    public void setState(String s) { state = s; }
    public void setCountry(String c) { country = c; }
    public void settype(String s) { type = s; }
    public void setCreated(LocalDate d) { created = d; }
    public void setLastUsed(LocalDate d) { lastUsed = d; }

    public String getName() { return name; }
    public String getState() { return state;}
    public String getCountry() { return country; }
    public String getType() { return type; }
    public LocalDate getCreated() { return created; }
    public LocalDate getLastUsed() { return lastUsed; }
}
