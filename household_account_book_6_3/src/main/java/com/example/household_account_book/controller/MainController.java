package com.example.household_account_book.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;

@Controller
@RequestMapping("/main")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final CategoryService categoryService;

    public MainController(ExpenseService expenseService, IncomeService incomeService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String main(Model model) {
        logger.info("メイン画面にアクセスされました。");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userName = userDetails.getDisplayName();
            User loggedInUser = userDetails.getUser();
            logger.info("ログインユーザー名: {}", userName);
            model.addAttribute("userName", userName);

            // 支出の円グラフデータ (カテゴリ名をキーとする)
            Map<String, BigDecimal> expenseByCategory = new HashMap<>();
            Map<String, String> expenseChartColors = new HashMap<>();
            Map<Category, BigDecimal> expenseByCategoryEntity = expenseService.getTotalExpensesByCategory(loggedInUser);
            for (Map.Entry<Category, BigDecimal> entry : expenseByCategoryEntity.entrySet()) {
                Category category = entry.getKey();
                expenseByCategory.put(category.getCategoryName(), entry.getValue());
                expenseChartColors.put(category.getCategoryName(),
                        category.getColor() != null ? category.getColor().getHexCode() : "gray");
            }
            model.addAttribute("expenseByCategory", expenseByCategory);
            model.addAttribute("expenseChartColors", expenseChartColors);

            // 収入の円グラフデータ (カテゴリ名をキーとする)
            Map<String, BigDecimal> incomeByCategory = new HashMap<>();
            Map<String, String> incomeChartColors = new HashMap<>();
            Map<Category, BigDecimal> incomeByCategoryEntity = incomeService.getTotalIncomesByCategory(loggedInUser);
            for (Map.Entry<Category, BigDecimal> entry : incomeByCategoryEntity.entrySet()) {
                Category category = entry.getKey();
                incomeByCategory.put(category.getCategoryName(), entry.getValue());
                incomeChartColors.put(category.getCategoryName(),
                        category.getColor() != null ? category.getColor().getHexCode() : "lightgray");
            }
            model.addAttribute("incomeByCategory", incomeByCategory);
            model.addAttribute("incomeChartColors", incomeChartColors);

            return "main";
        } else {
            logger.warn("認証情報が存在しないか、期待される型ではありません。ログイン画面にリダイレクトします。");
            return "redirect:/login";
        }
    }
}