package com.example.demo.dto;

import com.example.demo.domain.Location;

import java.util.List;

public class LocationDTO {
    private List<Location> Location;

    public LocationDTO(List<Location> dt) {
        Location = dt;
    }

    public List<Location> getLocation() { return Location; }
    public void setLocation(List<Location> d) { Location = d; }
}
