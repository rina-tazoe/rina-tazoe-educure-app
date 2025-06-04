package com.example.household_account_book.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.entity.Color;
import com.example.household_account_book.repository.CategoryRepository;
import com.example.household_account_book.repository.ColorRepository;

@Service
@Transactional 
public class ColorServiceImpl implements ColorService {

    private static final Logger logger = LoggerFactory.getLogger(ColorServiceImpl.class);
    private final ColorRepository colorRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ColorServiceImpl(ColorRepository colorRepository, CategoryRepository categoryRepository) { // コンストラクタを変更
        this.colorRepository = colorRepository;
        this.categoryRepository = categoryRepository; 
    }

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public List<Color> getAllColors() {
        List<Color> colors = findAll();
        logger.info("取得した色のリスト: {}", colors); // ★ ログ出力 ★
        return colors;
    }

    @Override
    public Optional<Color> findByColorName(String colorName) {
        return colorRepository.findByColorName(colorName);
    }

    @Override
    public Optional<Color> findById(Integer id) {
        return colorRepository.findById(id);
    }

    @Override
    public void save(Color color) {
        colorRepository.save(color);
    }

    @Override
    public void delete(Integer id) {
        Color colorToDelete = colorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Color not found with ID: " + id));

        List<Category> categoriesUsingColor = categoryRepository.findByColor(colorToDelete); 
        for (Category category : categoriesUsingColor) {
            
            category.setColor(null);
        }
        categoryRepository.saveAll(categoriesUsingColor); 

       
        colorRepository.deleteById(id);
    }
}