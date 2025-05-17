package com.example.household_account_book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByUserOrderByDateDesc(User user);

    List<Expense> findByUser(User user);

}