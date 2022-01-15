package com.example.demo.controllers;

import com.example.demo.domain.Location;
import com.example.demo.dto.LocationRowDTO;
import com.example.demo.dto.LocationTableDTO;
import com.example.demo.dto.LocationDTO;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class LocationController {

    @Autowired
    private LocationService service;

    @GetMapping(value = "/Location", produces = "application/json")
    public @ResponseBody
    LocationDTO getLocation() {
        List<Location> data = service.findAll();
        LocationDTO ret = new LocationDTO(data);

        return ret;
    }

    @GetMapping(value = "/locationtable", produces = "application/json")
    public @ResponseBody
    LocationTableDTO getLocationTable() {
        List<Location> data = service.findAll();

        List<LocationRowDTO> tdata = data.stream().map(LocationRowDTO::new).collect(Collectors.toList());

        return new LocationTableDTO(tdata);
    }
}
