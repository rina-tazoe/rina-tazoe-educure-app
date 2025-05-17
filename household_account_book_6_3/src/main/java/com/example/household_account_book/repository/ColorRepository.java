package com.example.household_account_book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.household_account_book.entity.Color;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    Optional<Color> findByColorName(String colorName); 
}