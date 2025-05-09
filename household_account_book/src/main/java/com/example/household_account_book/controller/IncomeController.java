package com.example.household_account_book.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.form.RecordInputForm;
import com.example.household_account_book.repository.IncomeRepository;
import com.example.household_account_book.repository.UserRepository;
import com.example.household_account_book.service.CategoryService;

@Controller
public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);
    private final CategoryService categoryService;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncomeController(CategoryService categoryService, IncomeRepository incomeRepository, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/income/input")
    public String incomeInput(Model model) {
        logger.info("/income/input が呼び出されました"); // ★ 追加 ★
        model.addAttribute("recordInputForm", new RecordInputForm()); // ★ 修正 ★
        model.addAttribute("today", LocalDate.now());
        List<Category> incomeCategories = categoryService.findIncomeCategories();
        List<Category> expenseCategories = categoryService.findExpenseCategories();
        logger.info("収入カテゴリ: {}", incomeCategories); // ★ 追加 ★
        logger.info("支出カテゴリ: {}", expenseCategories); // ★ 追加 ★
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        return "income-input";
    }
}