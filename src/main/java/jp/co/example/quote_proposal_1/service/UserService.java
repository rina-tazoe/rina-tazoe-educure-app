package jp.co.example.quote_proposal_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 新規ユーザー登録
    public User registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("指定されたユーザー名は既に存在します。");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ★★★ ここを修正 ★★★
        // user.getRoleId() を使ってロールIDの有無をチェック
        if (user.getRoleId() == null) {
            // デフォルトロールIDを設定するなどのロジック
            // 例: 新規登録は常に一般ユーザー(ロールID 2)にする
            user.setRoleId(2L); // デフォルトをユーザーロール(ID 2)とする
            // または、このサービスメソッドの呼び出し元（Controllerなど）でロールIDを設定するように強制
            // throw new IllegalArgumentException("ロールIDが指定されていません。");
        }
        // ★★★ 修正ここまで ★★★

        return userRepository.save(user);
    }

    // 他のユーザー関連のビジネスロジック
}