package com.example.household_account_book.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/delete")
    public String showDeleteConfirmation(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("userName", userDetails.getUsername());
        }
        return "deleteConfirmation";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                             HttpServletRequest request, HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            userService.deleteUser(username);

            // Spring Securityでログアウト処理を行う
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            redirectAttributes.addFlashAttribute("message", "退会処理が完了しました。");
            return "redirect:/login?logout";
        }
        return "redirect:/login"; 
    }
}