package com.example.demo.dto.ui;

import com.example.demo.bean.Lvd;

import java.util.List;
import java.util.Vector;

public class CategoriesUIDTO {
    private List<Lvd> categories;

    public CategoriesUIDTO() { categories = new Vector<Lvd>(); }
    public CategoriesUIDTO(List<Lvd> dt) {
        categories = dt;
    }

    public List<Lvd> getCategories() { return categories; }
    public void setCategories(List<Lvd> d) { categories = d; }
}
