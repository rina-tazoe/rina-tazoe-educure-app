package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.ExpenseRepository;
import com.example.household_account_book.repository.UserRepository; // ★ 追加 ★

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

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    public List<Expense> findUserExpenses(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(expenseRepository::findByUserOrderByDateDesc).orElse(List.of());
    }
}