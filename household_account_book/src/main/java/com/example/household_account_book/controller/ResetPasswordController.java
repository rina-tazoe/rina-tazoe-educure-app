package com.example.household_account_book.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.household_account_book.entity.User;
import com.example.household_account_book.service.UserService;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(@RequestParam("email") String email,
                                    @RequestParam("newPassword") String newPassword,
                                    Model model) {
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.updatePassword(user, newPassword);
            model.addAttribute("message", "パスワードが再設定されました。");
            return "redirect:/login";
        } else {
            model.addAttribute("message", "指定されたメールアドレスのユーザーは存在しません。");
            return "reset-password";
        }
    }
}