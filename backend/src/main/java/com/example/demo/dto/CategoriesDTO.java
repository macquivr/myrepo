package com.example.demo.dto;

import com.example.demo.domain.Category;

import java.util.List;

public class CategoriesDTO {
    private List<Category> categories;

    public CategoriesDTO(List<Category> dt) {
        categories = dt;
    }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> d) { categories = d; }
}
