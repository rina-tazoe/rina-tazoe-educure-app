package com.example.household_account_book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;
import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final UserService userService;

    @Autowired
    public ExpenseController(IncomeService incomeService, ExpenseService expenseService, UserService userService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String expenseList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();

            List<Income> userIncomes = incomeService.findUserIncomes(loggedInUser);
            List<Expense> userExpenses = expenseService.findUserExpenses(loggedInUser);
            model.addAttribute("incomes", userIncomes);
            model.addAttribute("expenses", userExpenses);
            return "income-list"; // テンプレート名は変更しない
        } else {
            return "redirect:/login";
        }
    }
}