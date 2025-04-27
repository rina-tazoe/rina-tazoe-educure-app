package com.example.household_account_book.controller;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.IncomeService;
import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public IncomeController(IncomeService incomeService, CategoryService categoryService, UserService userService) {
        this.incomeService = incomeService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/input")
    public String incomeInputForm(Model model) {
        model.addAttribute("income", new Income());
        model.addAttribute("incomeCategories", categoryService.findIncomeCategories());
        model.addAttribute("expenseCategories", categoryService.findExpenseCategories());
        model.addAttribute("today", LocalDate.now());
        return "income-input";
    }

    @PostMapping("/input")
    public String saveIncome(@ModelAttribute Income income, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            Optional<User> userOptional = userService.findById(userId);
            userOptional.ifPresent(income::setUser);
            incomeService.save(income);
            return "redirect:/income/list";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/list")
    public String incomeList(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            Optional<User> userOptional = userService.findById(userId);
            userOptional.ifPresent(user -> model.addAttribute("incomes", incomeService.findUserIncomes(user)));
            return "income-list";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/edit/{id}")
    public String incomeEditForm(@PathVariable Integer id, Model model) {
        Optional<Income> incomeOptional = incomeService.findById(id);
        incomeOptional.ifPresent(income -> {
            model.addAttribute("income", income);
            model.addAttribute("incomeCategories", categoryService.findIncomeCategories());
            model.addAttribute("expenseCategories", categoryService.findExpenseCategories());
        });
        return "income-edit";
    }

    @PostMapping("/edit")
    public String updateIncome(@ModelAttribute Income income) {
        incomeService.save(income);
        return "redirect:/income/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Integer id) {
        incomeService.findById(id).ifPresent(incomeService::delete);
        return "redirect:/income/list";
    }
}