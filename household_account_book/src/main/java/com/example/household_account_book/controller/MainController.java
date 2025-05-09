package com.example.household_account_book.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.household_account_book.entity.CustomUserDetails; // ★ この行を追加 ★

@Controller
@RequestMapping("/main")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    public String main(Model model) {
        logger.info("メイン画面にアクセスされました。");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userName = userDetails.getDisplayName();
            logger.info("ログインユーザー名: {}", userName);
            model.addAttribute("userName", userName);
            // 円グラフのデータをServiceから取得してModelに追加
            return "main";
        } else {
            logger.warn("認証情報が存在しないか、期待される型ではありません。ログイン画面にリダイレクトします。");
            return "redirect:/login";
        }
    }
}