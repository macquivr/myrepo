package com.example.demo.dto;

import com.example.demo.domain.Category;
import com.example.demo.domain.Location;
import com.example.demo.domain.Names;
import com.example.demo.misc.Nbucket;

import java.util.List;
import java.util.Vector;

public class NameLocCatDTO {
    private String state = null;
    private final List<Nbucket> names;
    private final List<Nbucket> locations;
    private final List<Nbucket> categories;

    public NameLocCatDTO(List<Names> nl,List<Location> ll, List<Category> cl)
    {
        names = new Vector<>();
        locations = new Vector<>();
        categories = new Vector<>();

        for (Names n : nl) {
            Nbucket nb = new Nbucket();
            nb.setName(n.getName());
            names.add(nb);
        }

        for (Location l : ll) {
            if (l.getType().equals("REG")) {
                Nbucket nb = new Nbucket();
                nb.setName(l.getName());
                locations.add(nb);
            }
        }

        for (Category c : cl) {
            Nbucket nb = new Nbucket();
            nb.setName(c.getName());
            categories.add(nb);
        }

    }

    public void setState(String s) { state = s; }

    public String getState() { return state; }
    public List<Nbucket> getNames() { return names; }
    public List<Nbucket> getLocations() { return locations; }
    public List<Nbucket> getCategories() { return categories; }

}
