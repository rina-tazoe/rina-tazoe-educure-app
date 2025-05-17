package com.example.household_account_book.controller;

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

import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.form.RecordInputForm;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ExpenseService;
import com.example.household_account_book.service.IncomeService;
import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final IncomeService incomeService;
    @Autowired
    private UserService userService; 

    @Autowired
    public ExpenseController(ExpenseService expenseService, CategoryService categoryService, IncomeService incomeService, UserService userService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String showExpenseList(Model model, Authentication authentication) {
        logger.info("/expenses/list が呼び出されました");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User loggedInUser = userService.findByUsername(username).orElse(null);

        List<Expense> expenses = expenseService.findUserExpenses(loggedInUser);
        List<Income> incomes = incomeService.findUserIncomes(loggedInUser);

        model.addAttribute("expenses", expenses);
        model.addAttribute("incomes", incomes); 
        return "account-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Integer id) {
        logger.info("/expenses/delete/{} が呼び出されました", id);
        expenseService.deleteExpense(id);
        return "redirect:/account/list"; // 削除後に収支一覧画面へリダイレクト
    }

    @GetMapping("/edit/{id}")
    public String redirectToCommonExpenseEdit(@PathVariable Integer id) {
        logger.info("/expenses/edit/{} から /record/edit/expense/{} へリダイレクト", id, id);
        return "redirect:/record/edit/expense/" + id;
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Integer id, @ModelAttribute RecordInputForm form, RedirectAttributes redirectAttributes) {
        logger.info("/expenses/update/{} から /record/update/expense/{} へリダイレクト", id, id);
        redirectAttributes.addFlashAttribute("recordInputForm", form);
        return "redirect:/record/update/expense/" + id;
    }
}