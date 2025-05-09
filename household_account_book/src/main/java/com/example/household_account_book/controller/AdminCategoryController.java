package com.example.household_account_book.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("newCategory", new Category());
        return "admin/category-list";
    }

    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        // 色の選択肢を定義
        List<String> colorOptions = Arrays.asList("赤", "緑", "青", "黄", "ピンク", "水色", "オレンジ", "紫", "白", "黒");
        model.addAttribute("colorOptions", colorOptions);
        return "admin/category-add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        categoryService.findById(id).ifPresent(category -> model.addAttribute("category", category));
        List<String> colorOptions = Arrays.asList("赤", "緑", "青", "黄", "ピンク", "水色", "オレンジ", "紫", "白", "黒");
        model.addAttribute("colorOptions", colorOptions);
        return "admin/category-edit";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Integer id, @ModelAttribute Category category) {
        category.setCategoryId(id); 
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/inline-edit/{id}")
    public String inlineEditCategory(@PathVariable Integer id,
                                     @RequestParam("categoryName") String categoryName,
                                     @RequestParam("color") String color,
                                     @RequestParam("categoryType") String categoryType) { 
        categoryService.findById(id).ifPresent(category -> {
            category.setCategoryName(categoryName);
            category.setColor(color);
            category.setCategoryType(categoryType); 
            categoryService.save(category);
        });
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "カテゴリを削除しました。");
        return "redirect:/admin/categories";
    }
}