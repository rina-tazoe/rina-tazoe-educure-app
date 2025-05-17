package com.example.household_account_book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    List<Income> findByUser(User user);

    List<Income> findByUser_UserId(int userId);

    List<Income> findByUserOrderByDateDesc(User user);

    List<Income> findByUser_UserIdOrderByDateDesc(int userId); 
    
}