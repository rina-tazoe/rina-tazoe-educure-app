package com.example.household_account_book.repository;

import com.example.household_account_book.entity.Expense;
import com.example.household_account_book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByUserOrderByDateDesc(User user);
}