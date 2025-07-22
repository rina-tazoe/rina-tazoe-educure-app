package jp.co.example.quote_proposal_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserList(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user_list"; // あなたのテンプレート名に合わせて修正 (user_list.html)
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        // ロール選択肢を渡す場合 (HTMLでselectタグを使うなど)
        model.addAttribute("roles", List.of("ADMIN", "USER")); // データベースのrolesテーブルのnameカラム値
        return "users/new"; // あなたのテンプレート名に合わせて修正 (users/new.html)
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerNewUser(
            @ModelAttribute User user, // フォームからユーザー名、パスワード、ロールを受け取る
            @RequestParam String passwordConfirm,
            RedirectAttributes redirectAttributes) {
        try {
            if (!user.getPassword().equals(passwordConfirm)) {
                redirectAttributes.addFlashAttribute("error", "パスワードとパスワード再確認が一致しません。");
                return "redirect:/users/new";
            }
            
            // ★ここが重要★ フォームからロールを受け取れない場合、ここでADMINを決め打ち
            // user.setRole("ADMIN"); // このように決め打ちすることも可能だが、フォームから渡す方が柔軟

            userService.registerNewUser(user);
            redirectAttributes.addFlashAttribute("message", "ユーザーが正常に登録されました。");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/new";
        }
    }
}