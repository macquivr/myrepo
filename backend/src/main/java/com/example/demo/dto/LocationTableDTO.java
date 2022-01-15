package com.example.demo.dto;


import java.util.List;

public class LocationTableDTO {
    private List<LocationRowDTO> Location;

    public LocationTableDTO(List<LocationRowDTO> dt) {
        Location = dt;
    }

    public List<LocationRowDTO> getLocation() { return Location; }
    public void setLocation(List<LocationRowDTO> d) { Location = d; }
}
