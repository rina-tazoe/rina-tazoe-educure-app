package com.example.household_account_book.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.form.RecordInputForm;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService; // ★ インポート ★
import com.example.household_account_book.service.IncomeService;
import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/")
public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);
    private final IncomeService incomeService;
    private final CategoryService categoryService;
    private final ExpenseService expenseService;
    private final UserService userService; 

    @Autowired
    public IncomeController(IncomeService incomeService, CategoryService categoryService, ExpenseService expenseService, UserService userService) {
        this.incomeService = incomeService;
        this.categoryService = categoryService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/income/input")
    public String incomeInput(Model model) {
        logger.info("/income/input が呼び出されました");
        model.addAttribute("recordInputForm", new RecordInputForm());
        model.addAttribute("today", LocalDate.now());
        List<Category> incomeCategories = categoryService.findIncomeCategories();
        List<Category> expenseCategories = categoryService.findExpenseCategories();
        logger.info("収入カテゴリ: {}", incomeCategories);
        logger.info("支出カテゴリ: {}", expenseCategories);
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        return "income-input";
    }

    @GetMapping("/income/list")
    public String showIncomeList(Model model, Authentication authentication) {
        logger.info("/income/list が呼び出されました");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User loggedInUser = userService.findByUsername(username).orElse(null);

        List<Income> incomes = incomeService.findUserIncomes(loggedInUser);
        List<Expense> expenses = expenseService.findUserExpenses(loggedInUser);
        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);
        return "income-list";
    }

    @GetMapping("/income/edit/{id}")
    public String redirectToCommonIncomeEdit(@PathVariable Integer id) {
        logger.info("/income/edit/{} から /record/edit/income/{} へリダイレクト", id, id);
        return "redirect:/record/edit/income/" + id;
    }

    @PostMapping("/income/update/{id}")
    public String updateIncome(@PathVariable Integer id, @ModelAttribute RecordInputForm form, RedirectAttributes redirectAttributes) {
        logger.info("/income/update/{} から /record/update/income/{} へリダイレクト", id, id);
        redirectAttributes.addFlashAttribute("recordInputForm", form);
        return "redirect:/record/update/income/" + id;
    }

    @GetMapping("/income/delete/{id}")
    public String deleteIncome(@PathVariable Integer id, Authentication authentication) {
        logger.info("/income/delete/{} が呼び出されました", id);
        incomeService.deleteIncome(id);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User loggedInUser = userService.findByUsername(username).orElse(null);

        return "redirect:/account/list";
    }

    
}