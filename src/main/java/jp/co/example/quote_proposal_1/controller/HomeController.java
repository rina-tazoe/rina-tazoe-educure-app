package jp.co.example.quote_proposal_1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ② ホーム画面の表示
    @GetMapping("/home")
    public String home(Model model) {
        // ログイン中のユーザー名を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            // ユーザーのロールに応じて表示を制御する場合（Thymeleaf側でも制御可能）
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))); // ロール名が "ROLE_ADMIN" の場合
        }
        return "home"; // src/main/resources/templates/home.html を表示
    }
}