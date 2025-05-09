package com.example.household_account_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.household_account_book.service.CategoryService;

@Controller
public class CategoryController { // クラスレベルの @RequestMapping は削除

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}