package com.example.demo.state.importer;

import com.example.demo.domain.Category;
import com.example.demo.domain.Names;
import com.example.demo.domain.Location;
import com.example.demo.importer.Repos;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.NamesRepository;

public class NewData {
    private String name = null;
    private String location = null;
    private String category = null;
    private boolean newName = false;
    private boolean newLocation = false;
    private boolean newCategory = false;

    private NewDataE state;

    public NewData() {
        state = NewDataE.NAME;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public boolean isNewName() { return newName; }
    public boolean isNewLocation() { return newLocation; }
    public boolean isNewCategory() { return newCategory; }

    public NewDataE getState() { return state; }

    public void reset() { state = NewDataE.NAME; }

    public void update(String data, Repos r) {

        if (state == NewDataE.NAME) {
            NamesRepository nrepo = r.getNamesRepository();
            Names nm = nrepo.findByName(data);
            newName = (nm == null);

            name = data;
            state = NewDataE.LOCATION;
            return;
        }

        if (state == NewDataE.LOCATION) {
            LocationRepository locRepo = r.getLocationRepository();
            Location loc = locRepo.findByName(data);
            newLocation = (loc == null);

            location = data;
            state = NewDataE.CATEGORY;
            return;
        }

        if (state == NewDataE.CATEGORY) {
            CategoryRepository catRepo = r.getCategoryRepository();
            Category cat = catRepo.findByName(data);
            newCategory = (cat == null);

            category = data;
            state = NewDataE.DONE;
        }
    }
}
