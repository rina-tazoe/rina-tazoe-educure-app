package com.example.household_account_book.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping
    public String main(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            //  円グラフのデータをServiceから取得してModelに追加
            return "main";
        } else {
            return "redirect:/login";
        }
    }
}