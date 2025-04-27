package com.example.household_account_book.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    public String main(Model model) {
        logger.info("メイン画面にアクセスされました。");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userName = userDetails.getUsername(); // ユーザー名を取得
            logger.info("ログインユーザー名: {}", userName);
            model.addAttribute("userName", userName); // モデルにユーザー名を追加
            // TODO: 円グラフのデータをServiceから取得してModelに追加
            return "main";
        } else {
            logger.warn("認証情報が存在しないため、ログイン画面にリダイレクトします。");
            return "redirect:/login";
        }
    }
}