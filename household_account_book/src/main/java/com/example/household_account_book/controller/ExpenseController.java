package com.example.household_account_book.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.form.ExpenseInputForm;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String expenseList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();

            List<Expense> userExpenses = expenseService.findUserExpenses(loggedInUser);
            model.addAttribute("expenses", userExpenses);
            return "expense/list";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/input")
    public String showExpenseInputForm(Model model) {
        List<Category> expenseCategories = categoryService.findByType("支出");
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("expenseInputForm", new ExpenseInputForm()); // ★ 変更 ★
        model.addAttribute("today", java.time.LocalDate.now());
        return "expense/input";
    }

    @PostMapping("/input")
    public String recordExpense(@ModelAttribute("expenseInputForm") ExpenseInputForm expenseInputForm) { // ★ 変更 ★
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();

            Expense expense = new Expense();
            expense.setUser(loggedInUser);
            expense.setDate(expenseInputForm.getDate());
            expense.setDescription(expenseInputForm.getDescription());
            expense.setAmount(expenseInputForm.getAmount());

            Optional<Category> categoryOptional = categoryService.findById(expenseInputForm.getExpenseCategoryId());
            if (categoryOptional.isPresent()) {
                expense.setCategory(categoryOptional.get());
                expenseService.recordExpense(expense);
                return "redirect:/expenses/list";
            } else {
                logger.error("支出カテゴリが見つかりませんでした。categoryId: {}", expenseInputForm.getExpenseCategoryId());
                return "redirect:/expenses/input?error=categoryNotFound";
            }
        } else {
            return "redirect:/login";
        }
    }
}