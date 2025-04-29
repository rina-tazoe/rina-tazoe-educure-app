package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.IncomeRepository;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<Income> findUserIncomes(User user) {
        return incomeRepository.findByUserOrderByDateDesc(user);
    }

    public Income save(Income income) {
        return incomeRepository.save(income);
    }

    public Optional<Income> findById(Integer id) {
        return incomeRepository.findById(id);
    }

    public void delete(Income income) {
        incomeRepository.delete(income);
    }
}