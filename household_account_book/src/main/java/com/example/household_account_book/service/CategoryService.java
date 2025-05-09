package com.example.household_account_book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findIncomeCategories() {
        return categoryRepository.findByCategoryType("収入");
    }

    public List<Category> findExpenseCategories() {
        return categoryRepository.findByCategoryType("支出");
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findByType(String type) {
        return categoryRepository.findByCategoryType(type);
    }

    public Category save(Category category) {
        if (category.getCategoryId() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    // 論理削除のメソッド（必要であれば残しておく）
    // public void delete(Category category) {
    //     category.setDeletedAt(LocalDateTime.now());
    //     categoryRepository.save(category); // 論理削除
    // }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id); // ★ 物理削除に変更 ★
    }
}