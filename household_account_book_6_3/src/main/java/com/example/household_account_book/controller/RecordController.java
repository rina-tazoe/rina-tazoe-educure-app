package com.example.household_account_book.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.form.RecordInputForm;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;
import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/record")
public class RecordController {

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    @Autowired
    private UserService userService;

    @Autowired
    public RecordController(IncomeService incomeService, ExpenseService expenseService, CategoryService categoryService, UserService userService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/edit/{type}/{id}")
    public String showEditForm(@PathVariable String type, @PathVariable Integer id, Model model) {
        logger.info("/record/edit/{}/{} が呼び出されました", type, id);
        RecordInputForm form = new RecordInputForm();
        model.addAttribute("recordType", type);
        model.addAttribute("recordId", id);

        List<Category> categories = null;
        if (type.equals("income")) {
            categories = categoryService.findIncomeCategories();
            Optional<Income> incomeOptional = incomeService.findIncomeById(id);
            if (incomeOptional.isPresent()) {
                Income income = incomeOptional.get();
                form.setDate(income.getDate());
                form.setType(income.getType());
                form.setCategoryId(income.getCategory().getCategoryId());
                form.setDescription(income.getDescription());
                form.setAmount(income.getAmount());
                model.addAttribute("recordInputForm", form);
                model.addAttribute("record", income);
            }
        } else if (type.equals("expense")) {
            categories = categoryService.findExpenseCategories();
            Optional<Expense> expenseOptional = expenseService.findById(id);
            if (expenseOptional.isPresent()) {
                Expense expense = expenseOptional.get();
                form.setDate(expense.getDate());
                form.setType(expense.getType());
                form.setCategoryId(expense.getCategory().getCategoryId());
                form.setDescription(expense.getDescription());
                form.setAmount(expense.getAmount());
                model.addAttribute("recordInputForm", form);
                model.addAttribute("record", expense);
            }
        }
        model.addAttribute("categories", categories); // 共通のカテゴリリスト
        return "record-edit";
    }

    @PostMapping("/update/{type}/{id}")
    public String updateRecord(@PathVariable String type, @PathVariable Integer id, @ModelAttribute("recordInputForm") RecordInputForm form) {
        logger.info("/record/update/{}/{} が呼び出されました", type, id);
        Category category = categoryService.findCategoryById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category Id:" + form.getCategoryId()));

        if (type.equals("income")) {
            Optional<Income> incomeOptional = incomeService.findIncomeById(id);
            if (incomeOptional.isPresent()) {
                Income income = incomeOptional.get();
                income.setDate(form.getDate());
                income.setType(form.getType());
                income.setCategory(category);
                income.setDescription(form.getDescription());
                income.setAmount(form.getAmount());
                incomeService.recordIncome(income);
            }
        } else if (type.equals("expense")) {
            Optional<Expense> expenseOptional = expenseService.findById(id);
            if (expenseOptional.isPresent()) {
                Expense expense = expenseOptional.get();
                expense.setDate(form.getDate());
                expense.setType(form.getType());
                expense.setCategory(category);
                expense.setDescription(form.getDescription());
                expense.setAmount(form.getAmount());
                expenseService.recordExpense(expense);
            }
        }
        return "redirect:/account/list";
    }

    @GetMapping("/input")
    public String showInputForm(Model model) {
        logger.info("/record/input が呼び出されました");
        model.addAttribute("recordInputForm", new RecordInputForm());
        model.addAttribute("incomeCategories", categoryService.findIncomeCategories());
        model.addAttribute("expenseCategories", categoryService.findExpenseCategories());
        model.addAttribute("today", java.time.LocalDate.now());
        return "income-input"; // 現在のファイル名を維持
    }

    @PostMapping("/input")
    public String registerRecord(@ModelAttribute RecordInputForm form, Authentication authentication) {
        logger.info("/record/input (POST) が呼び出されました: {}", form);
        Category category = categoryService.findCategoryById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category Id:" + form.getCategoryId()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername(); // ★ メールアドレスを取得 ★
        Optional<User> loggedInUserOptional = userService.findByEmail(email); // ★ メールアドレスで検索 ★
        User loggedInUser = loggedInUserOptional.orElse(null);

        // ★★★ ここからログ出力 ★★★
        logger.info("取得したログインユーザー: {}", loggedInUser);
        if (loggedInUser != null) {
            logger.info("ログインユーザーのID: {}", loggedInUser.getUserId());
        } else {
            logger.warn("ログインユーザーは null です！");
        }
        // ★★★ ここまでログ出力 ★★★

        if (form.getType().equals("収入")) {
            Income income = new Income();
            income.setDate(form.getDate());
            income.setType(form.getType());
            income.setCategory(category);
            income.setDescription(form.getDescription());
            income.setAmount(form.getAmount());
            income.setUser(loggedInUser);
            incomeService.recordIncome(income);
        } else if (form.getType().equals("支出")) {
            Expense expense = new Expense();
            expense.setDate(form.getDate());
            expense.setType(form.getType());
            expense.setCategory(category);
            expense.setDescription(form.getDescription());
            expense.setAmount(form.getAmount());
            expense.setUser(loggedInUser); 
            expenseService.recordExpense(expense);
        }
        return "redirect:/account/list";
    }
}