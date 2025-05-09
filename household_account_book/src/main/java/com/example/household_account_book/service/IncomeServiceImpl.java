package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.IncomeRepository;
import com.example.household_account_book.repository.UserRepository; // ★ 追加 ★

@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository; 

    @Override
    public void recordIncome(Income income) {
        incomeRepository.save(income);
    }

    @Override
    public List<Income> findUserIncomes(User user) {
        return incomeRepository.findByUserOrderByDateDesc(user);
    }

    @Override
    public Optional<Income> findById(Integer id) {
        return incomeRepository.findById(id);
    }

    @Override
    public void delete(Income income) {
        incomeRepository.delete(income);
    }

    @Override
    public List<Income> findUserIncomes(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(incomeRepository::findByUserOrderByDateDesc).orElse(List.of());
    }
}