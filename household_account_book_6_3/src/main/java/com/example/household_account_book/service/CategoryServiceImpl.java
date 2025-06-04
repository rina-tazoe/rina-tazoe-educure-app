package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Color;
import com.example.household_account_book.repository.CategoryRepository;
import com.example.household_account_book.repository.ColorRepository;
import com.example.household_account_book.repository.ExpenseRepository; 

@Service
@Transactional 
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class); 
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository; 
    private final ExpenseRepository expenseRepository; 

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ColorRepository colorRepository, 
                               ExpenseRepository expenseRepository) { 
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository; 
        this.expenseRepository = expenseRepository; 
    }

    @Override
    public List<Category> findIncomeCategories() {
        return categoryRepository.findByCategoryType("income");
    }

    @Override
    public List<Category> findExpenseCategories() {
        return categoryRepository.findByCategoryType("expense");
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
        categoryRepository.save(category);
    }


    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAllWithColor() {
        return categoryRepository.findAllWithColor();
    }

    @Override
    public Optional<Category> findByIdWithColor(Integer id) {
        return categoryRepository.findByIdWithColor(id);
    }

    @Override
    public void deleteById(Integer id) {
        Category categoryToDelete = categoryRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        logger.info("カテゴリID {} に紐づく費用を削除します...", id);
        expenseRepository.deleteByCategory(categoryToDelete);
        logger.info("カテゴリID {} に紐づく費用の削除が完了しました。", id);

        logger.info("カテゴリID {} を削除します...", id);
        categoryRepository.deleteById(id);
        logger.info("カテゴリID {} の削除が完了しました。", id);
    }
}