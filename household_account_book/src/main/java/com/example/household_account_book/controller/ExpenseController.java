package com.example.household_account_book.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.Income; // Income エンティティをインポート
import com.example.household_account_book.service.IncomeService; // IncomeService をインポート
import com.example.household_account_book.service.UserService; // UserService をインポート

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final IncomeService incomeService;
    private final UserService userService;

    @Autowired
    public ExpenseController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String expenseList(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            userService.findById(userId).ifPresent(user -> {
                List<Income> userIncomes = incomeService.findUserIncomes(user);
                model.addAttribute("incomes", userIncomes);
            });
            return "income-list"; // 以前作成した収支一覧の Thymeleaf テンプレート名
        } else {
            return "redirect:/login";
        }
    }
}