package com.example.household_account_book.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.household_account_book.entity.Category; // ★ 追加 ★
import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.IncomeRepository;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    @Autowired
    public IncomeServiceImpl(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    public void recordIncome(Income income) {
        incomeRepository.save(income);
    }

    @Override
    public List<Income> findUserIncomes(User user) {
        return incomeRepository.findByUserOrderByDateDesc(user);
    }

    @Override
    public Optional<Income> findIncomeById(Integer id) {
        return incomeRepository.findById(id);
    }

    @Override
    public void delete(Income income) {
        incomeRepository.delete(income);
    }

    @Override
    public List<Income> findUserIncomes(int userId) {
        return incomeRepository.findByUser_UserIdOrderByDateDesc(userId);
    }

    @Override
    public void deleteIncome(Integer id) {
        incomeRepository.deleteById(id);
    }

    @Override
    public Map<Category, BigDecimal> getTotalIncomesByCategory(User user) {
        List<Income> incomes = incomeRepository.findByUser(user);
        Map<Category, BigDecimal> totalIncomes = new HashMap<>();
        for (Income income : incomes) {
            Category category = income.getCategory(); 
            BigDecimal amount = income.getAmount();
            totalIncomes.put(category, totalIncomes.getOrDefault(category, BigDecimal.ZERO).add(amount)); // ★ Category をキーとして使用 ★
        }
        return totalIncomes;
    }
}