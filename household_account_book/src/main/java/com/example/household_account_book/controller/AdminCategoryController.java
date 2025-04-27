package com.example.household_account_book.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.entity.Category;
import com.example.household_account_book.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String categoryList(Model model, HttpSession session) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/category-list";
        } else {
            return "redirect:/main";
        }
    }

    @GetMapping("/add")
    public String addCategoryForm(Model model, HttpSession session) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            model.addAttribute("category", new Category());
            return "admin/category-add";
        } else {
            return "redirect:/main";
        }
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model, HttpSession session) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            categoryService.findById(id).ifPresent(category -> model.addAttribute("category", category));
            return "admin/category-edit";
        } else {
            return "redirect:/main";
        }
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            categoryService.findById(id).ifPresent(category -> {
                // 関連する収入記録がないか確認し、削除またはエラー処理を行う
                categoryService.delete(category);
                redirectAttributes.addFlashAttribute("message", "カテゴリ '" + category.getCategoryName() + "' を削除しました。");
            });
            return "redirect:/admin/category/list";
        } else {
            return "redirect:/main";
        }
    }
}