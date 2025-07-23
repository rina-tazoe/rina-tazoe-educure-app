package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // パスワードエンコーダーをインポート
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Role;
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.form.UserForm;
import jp.co.example.quote_proposal_1.repository.RoleRepository;
import jp.co.example.quote_proposal_1.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // パスワードエンコーダーを注入

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 全ユーザーを取得する
     * @return ユーザーのリスト
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 指定されたIDのユーザーを取得する
     * @param id ユーザーID
     * @return ユーザー (Optional)
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * ユーザー名でユーザーを取得する
     * @param username ユーザー名
     * @return ユーザー (Optional)
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 新規ユーザーを登録する
     * @param userForm ユーザーフォームデータ
     * @return 登録されたユーザーエンティティ
     * @throws IllegalArgumentException ロールが見つからない場合
     */
    public User registerUser(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        // パスワードをBCryptでエンコードして設定
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));

        // ロールIDからRoleエンティティを取得して設定
        Role role = roleRepository.findById(userForm.getRoleId())
                                  .orElseThrow(() -> new IllegalArgumentException("指定されたロールが見つかりません。ID: " + userForm.getRoleId()));
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * ユーザーを削除する
     * @param id 削除するユーザーのID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
