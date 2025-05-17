package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Color;

public interface CategoryService {
    List<Category> findIncomeCategories();
    List<Category> findExpenseCategories();
    List<Category> findAllCategories();
    Optional<Category> findCategoryById(Integer id);
    void save(Category category);
    void delete(Integer id);
    List<Color> getAllColors();
    List<Category> findAll();
    Optional<Category> findById(Integer id);
    void deleteById(Integer id);
    List<Category> findAllWithColor();
    Optional<Category> findByIdWithColor(Integer id);
}