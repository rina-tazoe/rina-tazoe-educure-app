package com.example.household_account_book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {

    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(@RequestParam("userId") String userId, // メールアドレスを使用する方が一般的
                                     @RequestParam("email") String email,
                                     @RequestParam("newPassword") String newPassword,
                                     Model model) {
        //  ユーザーIDまたはメールアドレスの存在確認、パスワードの更新処理
        // 成功したらログイン画面へリダイレクト、失敗したらエラーメッセージを表示
        model.addAttribute("message", "パスワードリセット機能を実装してください。");
        return "reset-password";
    }
}