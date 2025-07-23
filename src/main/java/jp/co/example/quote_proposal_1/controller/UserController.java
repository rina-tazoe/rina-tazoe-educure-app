package jp.co.example.quote_proposal_1.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.Role;
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.form.UserForm;
import jp.co.example.quote_proposal_1.service.RoleService;
import jp.co.example.quote_proposal_1.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * ユーザー一覧画面を表示する (管理者のみアクセス可能を想定)
     */
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        logger.info("ユーザー一覧を表示します。ユーザー数: {}", users.size());
        return "users/list"; // templates/users/list.html を表示
    }

    /**
     * 新規ユーザー登録フォームを表示する
     */
    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        List<Role> roles = roleService.findAllRoles(); // ロール選択肢を取得
        model.addAttribute("roles", roles);
        logger.info("新規ユーザー登録フォームを表示します。");
        return "users/new"; // templates/users/new.html を表示
    }

    /**
     * 新規ユーザーを登録する
     */
    @PostMapping("/new")
    public String registerUser(@Validated @ModelAttribute UserForm userForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        logger.info("新規ユーザー登録リクエストを受信しました: {}", userForm.getUsername());

        // パスワードと確認用パスワードの一致チェック
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userForm", "パスワードが一致しません");
        }

        // ユーザー名が既に存在するかチェック
        if (userService.findByUsername(userForm.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.userForm", "このユーザー名は既に存在します");
        }

        if (bindingResult.hasErrors()) {
            logger.warn("新規ユーザー登録バリデーションエラー: {}", bindingResult.getAllErrors());
            List<Role> roles = roleService.findAllRoles(); // ロール選択肢を再取得
            model.addAttribute("roles", roles);
            return "users/new"; // エラーがある場合はフォームに戻す
        }

        try {
            userService.registerUser(userForm);
            redirectAttributes.addFlashAttribute("message", "ユーザー「" + userForm.getUsername() + "」を登録しました。");
            logger.info("ユーザー「{}」を正常に登録しました。", userForm.getUsername());
        } catch (IllegalArgumentException e) {
            logger.error("ロール登録エラー: {}", e.getMessage());
            bindingResult.reject("error.userForm", e.getMessage());
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("roles", roles);
            return "users/new";
        } catch (Exception e) {
            logger.error("ユーザー登録中に予期せぬエラーが発生しました: {}", e.getMessage(), e);
            bindingResult.reject("error.userForm", "ユーザー登録中にエラーが発生しました。");
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("roles", roles);
            return "users/new";
        }

        return "redirect:/users"; // 登録成功後、ユーザー一覧へリダイレクト
    }

    /**
     * ユーザーを削除する (管理者のみアクセス可能を想定)
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("ユーザー削除リクエストを受信しました。ID: {}", id);
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "ユーザーID: " + id + " を削除しました。");
            logger.info("ユーザーID: {} を正常に削除しました。", id);
        } catch (Exception e) {
            logger.error("ユーザーID: {} の削除中にエラーが発生しました: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "ユーザーID: " + id + " の削除に失敗しました。");
        }
        return "redirect:/users"; // 削除後、ユーザー一覧へリダイレクト
    }
}
