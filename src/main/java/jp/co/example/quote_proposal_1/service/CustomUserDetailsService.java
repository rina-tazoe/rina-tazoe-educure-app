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

        // ★★★ 修正箇所: user.getRole().getName() を使用してロール名を取得 ★★★
        // Userエンティティのroleフィールド（Role型）から、そのnameプロパティにアクセスします。
        // Userエンティティのroleフィールドは@ManyToOneでnullable=falseなので、nullチェックは不要です。
        String roleName = user.getRole().getName();

        // Spring Securityのロール名には通常 "ROLE_" プレフィックスが付与されていることを確認
        // もしデータベースのroles.nameが "ADMIN" や "USER" の場合、ここで "ROLE_" を付与する必要があります。
        // data.sqlで "ROLE_ADMIN", "ROLE_USER" と挿入しているので、そのまま使用します。

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
