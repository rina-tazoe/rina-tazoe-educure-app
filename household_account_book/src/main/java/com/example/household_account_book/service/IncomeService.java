package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;

public interface IncomeService {
    void recordIncome(Income income);
    List<Income> findUserIncomes(User user);
    Optional<Income> findById(Integer id);
    void delete(Income income);
    List<Income> findUserIncomes(int userId);
}