package com.example.household_account_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.household_account_book.entity.User; // ★★★ User クラスをインポート ★★★
import com.example.household_account_book.service.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam("userName") String userName,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        userService.registerUser(userName, email, password);
        return "redirect:/login";
    }
}