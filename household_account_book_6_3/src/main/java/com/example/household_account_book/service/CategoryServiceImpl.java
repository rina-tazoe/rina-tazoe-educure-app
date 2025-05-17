package com.example.household_account_book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Color;
import com.example.household_account_book.repository.CategoryRepository;
import com.example.household_account_book.repository.ColorRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ColorRepository colorRepository) {
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
    }

    @Override
    public List<Category> findIncomeCategories() {
        return categoryRepository.findByCategoryType("収入");
    }

    @Override
    public List<Category> findExpenseCategories() {
        return categoryRepository.findByCategoryType("支出");
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void save(Category category) {
        if (category.getCategoryId() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAllWithColor();
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findByIdWithColor(id);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findAllWithColor() {
        return categoryRepository.findAllWithColor();
    }

    @Override
    public Optional<Category> findByIdWithColor(Integer id) {
        return categoryRepository.findByIdWithColor(id);
    }
}