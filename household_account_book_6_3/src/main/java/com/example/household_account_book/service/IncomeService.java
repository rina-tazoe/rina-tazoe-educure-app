package com.example.household_account_book.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.household_account_book.entity.Category; // ★ 追加 ★
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;

public interface IncomeService {

    void recordIncome(Income income);

    List<Income> findUserIncomes(User user);

    Optional<Income> findIncomeById(Integer id);

    void delete(Income income);

    List<Income> findUserIncomes(int userId);

    void deleteIncome(Integer id);


    Map<Category, BigDecimal> getTotalIncomesByCategory(User user); 
}