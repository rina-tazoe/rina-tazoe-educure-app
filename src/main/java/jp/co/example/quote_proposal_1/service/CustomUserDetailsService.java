package jp.co.example.quote_proposal_1.service;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.repository.UserRepository;

@Service // SpringがこのクラスをBeanとして管理するためのアノテーション
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // データベースからユーザー名を元にユーザー情報を取得
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        // ユーザーの権限（ロール）を設定
        // データベースのrolesテーブルのidとnameの対応に合わせて修正
        String roleName;
        if (user.getRoleId() == 1L) { // rolesテーブルでid=1がROLE_USER
            roleName = "ROLE_USER"; // user.getRoleId()が1LならROLE_USER
        } else if (user.getRoleId() == 2L) { // rolesテーブルでid=2がROLE_ADMIN
            roleName = "ROLE_ADMIN"; // user.getRoleId()が2LならROLE_ADMIN
        } else {
            // 未知のロールIDの場合のデフォルトやエラーハンドリング
            roleName = "ROLE_UNKNOWN"; // または例外をスロー
        }
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(roleName));

        // Spring SecurityのUserオブジェクトを構築して返す
        // パスワードはデータベースから取得したハッシュ化されたパスワードを使用
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
