package jp.co.example.quote_proposal_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // ① ログイン画面の表示
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // src/main/resources/templates/login.html を表示
    }

    // ログイン処理はSpring Securityが自動的に処理します。
    // 成功時は /home にリダイレクトされるようにSecurityConfigで設定します。
    // ログアウト処理もSpring Securityが自動的に処理します。
    // 成功時は /login にリダイレクトされるようにSecurityConfigで設定します。
}