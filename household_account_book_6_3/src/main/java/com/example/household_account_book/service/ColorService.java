package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import com.example.household_account_book.entity.Color;

public interface ColorService {
    List<Color> findAll();
    List<Color> getAllColors();
    Optional<Color> findByColorName(String colorName);
    Optional<Color> findById(Integer id);
    void save(Color color);
    void delete(Integer id);
}