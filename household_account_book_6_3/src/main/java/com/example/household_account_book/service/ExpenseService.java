package com.example.household_account_book.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.ExpenseRepository;
import com.example.household_account_book.repository.UserRepository;

@Service
@Transactional
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public void recordExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public List<Expense> findUserExpenses(User user) {
        return expenseRepository.findByUserOrderByDateDesc(user);
    }

    public Optional<Expense> findById(Integer id) {
        return expenseRepository.findById(id);
    }

    public void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> findUserExpenses(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(expenseRepository::findByUserOrderByDateDesc).orElse(List.of());
    }

    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    public Map<Category, BigDecimal> getTotalExpensesByCategory(User user) {
        List<Expense> expenses = expenseRepository.findByUser(user);
        Map<Category, BigDecimal> totalExpenses = new HashMap<>();
        for (Expense expense : expenses) {
            Category category = expense.getCategory();
            BigDecimal amount = expense.getAmount();
            totalExpenses.put(category, totalExpenses.getOrDefault(category, BigDecimal.ZERO).add(amount));
        }
        return totalExpenses;
    }
}