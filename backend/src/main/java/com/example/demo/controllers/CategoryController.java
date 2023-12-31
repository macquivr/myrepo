package com.example.demo.controllers;

import com.example.demo.domain.Category;
import com.example.demo.dto.CategoriesDTO;
import com.example.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping(value = "/categories", produces = "application/json")
    public @ResponseBody
    CategoriesDTO getCategories() {
        List<Category> data = service.findAll();
        return new CategoriesDTO(data);
    }
}
