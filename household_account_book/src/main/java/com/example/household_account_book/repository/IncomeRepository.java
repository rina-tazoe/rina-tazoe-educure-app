package com.example.household_account_book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.household_account_book.entity.Income;
import com.example.household_account_book.entity.User;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findByUserOrderByDateDesc(User user);
    // ★ 削除またはコメントアウト ★
    // List<Income> findByUserIdOrderByDateDesc(int userId);
}