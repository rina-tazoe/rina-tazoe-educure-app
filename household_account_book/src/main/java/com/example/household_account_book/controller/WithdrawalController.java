package com.example.household_account_book.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.household_account_book.entity.User; // ★★★ User クラスをインポート ★★★
import com.example.household_account_book.service.UserService;

@Controller
public class WithdrawalController {

    private final UserService userService;

    @Autowired
    public WithdrawalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/delete")
    public String withdrawalConfirm(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("loggedInUser");
        if (userId != null) {
            userService.findById(userId).ifPresent(user -> model.addAttribute("user", user)); // ★★★ model.addAttribute("user", user); に修正 ★★★
            return "withdrawal-confirm";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/user/delete")
    public String withdraw(@RequestParam("userId") Integer userId, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUser");
        if (loggedInUserId != null && loggedInUserId.equals(userId)) {
            Optional<User> userOptional = userService.findById(userId); // ★★★ Optional<User> で取得 ★★★
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // パスワードの照合（ハッシュ化されたパスワードとの比較）
                if (user.getPassword().equals(password)) {
                    userService.deleteUser(userId);
                    session.invalidate();
                    redirectAttributes.addFlashAttribute("message", "退会処理が完了しました。");
                    return "redirect:/login";
                } else {
                    redirectAttributes.addFlashAttribute("error", "パスワードが間違っています。");
                    return "redirect:/user/delete";
                }
            }
        }
        return "redirect:/login";
    }
}