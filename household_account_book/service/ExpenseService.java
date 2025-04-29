package com.example.household_account_book.service;

import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findUserExpenses(User user) {
        return expenseRepository.findByUserOrderByDateDesc(user);
    }

    public Optional<Expense> findById(Integer id) {
        return expenseRepository.findById(id);
    }

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }
}