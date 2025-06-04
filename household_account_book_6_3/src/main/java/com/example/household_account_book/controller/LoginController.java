package com.example.household_account_book.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.household_account_book.entity.User;
import com.example.household_account_book.service.UserService;

@Controller
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; 

    @Autowired
    public LoginController(UserService userService, PasswordEncoder passwordEncoder) { 
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // パスワードの照合（ハッシュ化されたパスワードとの比較）
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("loggedInUser", user.getUserId());
                session.setAttribute("userRole", user.getRole().getRoleName());
                return "redirect:/main";
            } else {
                model.addAttribute("error", "パスワードが間違っています。");
                return "login";
            }
        } else {
            model.addAttribute("error", "ユーザーIDが存在しません。");
            return "login";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "reset-password";
    }
}