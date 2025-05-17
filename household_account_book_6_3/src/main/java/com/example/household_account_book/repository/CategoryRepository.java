package com.example.household_account_book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.household_account_book.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryType(String categoryType);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.color")
    List<Category> findAllWithColor();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.color WHERE c.categoryId = :id")
    Optional<Category> findByIdWithColor(@Param("id") Integer id);

    @Override
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.color")
    List<Category> findAll();

    @Override
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.color WHERE c.categoryId = :id")
    Optional<Category> findById(Integer id);
}