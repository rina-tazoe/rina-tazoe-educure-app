package com.example.household_account_book.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(Model model, HttpSession session) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            model.addAttribute("users", userService.findAll());
            return "admin/user-list";
        } else {
            return "redirect:/main";
        }
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteUser(@PathVariable Integer id, Model model, HttpSession session) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            userService.findById(id).ifPresent(user -> model.addAttribute("userToDelete", user));
            return "admin/user-delete-confirm";
        } else {
            return "redirect:/main";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Integer userId, HttpSession session, RedirectAttributes redirectAttributes) {
        if ("管理者".equals(session.getAttribute("userRole"))) {
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("message", "ユーザーID " + userId + " を削除しました。");
            return "redirect:/admin/users";
        } else {
            return "redirect:/main";
        }
    }
}