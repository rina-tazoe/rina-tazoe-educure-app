package jp.co.example.quote_proposal_1.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm {

    @NotBlank(message = "ユーザー名は必須です")
    @Size(min = 3, max = 50, message = "ユーザー名は3文字以上50文字以下で入力してください")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$",
             message = "パスワードは半角英数字を組み合わせた8文字以上で入力してください")
    private String password;

    @NotBlank(message = "パスワード（確認）は必須です")
    private String confirmPassword;

    private Long roleId; // ロール選択用
}
