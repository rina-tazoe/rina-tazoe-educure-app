package com.example.household_account_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.User; // ★ 追加 ★
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/list")
    public String accountList(Model model) {
        // ログインしているユーザーの情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser(); // ログインユーザーの User エンティティを取得

            // ログインユーザーの収入データを取得
            model.addAttribute("incomes", incomeService.findUserIncomes(loggedInUser));
            // ログインユーザーの支出データを取得
            model.addAttribute("expenses", expenseService.findUserExpenses(loggedInUser.getUserId())); // ExpenseService のメソッドが userId を受け取る場合はそのまま

            // 収支一覧を表示するThymeleafテンプレート名を返す
            return "income-list";
        } else {
            // 認証されていない場合の処理 (エラーページへリダイレクト)
            return "error/unauthorized"; 
        }
    }
}