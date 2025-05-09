package com.example.household_account_book.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.form.RecordInputForm;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;

@Controller
public class RecordController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    @Autowired
    public RecordController(IncomeService incomeService, ExpenseService expenseService, CategoryService categoryService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/record/input")
    public String showRecordInputForm(Model model) {
        List<Category> incomeCategories = categoryService.findByType("収入");
        List<Category> expenseCategories = categoryService.findByType("支出");
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("recordInputForm", new RecordInputForm());
        model.addAttribute("today", LocalDate.now());
        return "record/input";
    }

    @PostMapping("/record/input")
    public String record(@ModelAttribute("recordInputForm") RecordInputForm recordInputForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();

            if ("収入".equals(recordInputForm.getType())) {
                Income income = new Income();
                income.setUser(loggedInUser);
                income.setDate(recordInputForm.getDate());
                income.setDescription(recordInputForm.getDescription());
                income.setAmount(new java.math.BigDecimal(recordInputForm.getAmount())); // ★ 修正 ★
                categoryService.findById(recordInputForm.getIncomeCategoryId())
                        .ifPresent(income::setCategory);
                incomeService.recordIncome(income);
                return "redirect:/income/list";
            } else if ("支出".equals(recordInputForm.getType())) {
                Expense expense = new Expense();
                expense.setUser(loggedInUser);
                expense.setDate(recordInputForm.getDate());
                expense.setDescription(recordInputForm.getDescription());
                expense.setAmount(recordInputForm.getAmount()); // Expense の amount 型を確認してください
                categoryService.findById(recordInputForm.getExpenseCategoryId())
                        .ifPresent(expense::setCategory);
                expenseService.recordExpense(expense);
                return "redirect:/expenses/list";
            } else {
                return "redirect:/record/input?error=invalidType";
            }
        } else {
            return "redirect:/login";
        }
    }
}