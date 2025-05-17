package com.example.household_account_book.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.household_account_book.entity.Color;
import com.example.household_account_book.service.CategoryService;
import com.example.household_account_book.service.ColorService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ColorService colorService;

    @Autowired
    public CategoryController(CategoryService categoryService, ColorService colorService) {
        this.categoryService = categoryService;
        this.colorService = colorService;
    }

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.findAllWithColor();
        List<Color> colorOptions = colorService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category());
        model.addAttribute("colorOptions", colorOptions); 
        return "admin/category-list";
    }

    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        List<Color> colorOptions = colorService.getAllColors();
        model.addAttribute("colorOptions", colorOptions);
        return "admin/category-add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, @RequestParam("colorId") Integer colorId) {
        colorService.findById(colorId).ifPresent(category::setColor);
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        categoryOptional.ifPresent(category -> {
            model.addAttribute("category", category);
            List<Color> colorOptions = colorService.getAllColors();
            model.addAttribute("colorOptions", colorOptions);
        });
        return "admin/category-edit";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Integer id, @ModelAttribute Category category, @RequestParam("colorId") Integer colorId) {
        category.setCategoryId(id);
        colorService.findById(colorId).ifPresent(category::setColor);
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/inline-edit/{id}")
    public String inlineEditCategory(@PathVariable Integer id,
                                     @RequestParam("categoryName") String categoryName,
                                     @RequestParam("colorId") Integer colorId,
                                     @RequestParam("categoryType") String categoryType) {
        categoryService.findById(id).ifPresent(category -> {
            category.setCategoryName(categoryName);
            colorService.findById(colorId).ifPresent(category::setColor);
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