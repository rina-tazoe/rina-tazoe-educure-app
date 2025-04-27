package com.example.household_account_book;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login") // ★ カスタムログインページのパスを指定 ★
                .loginProcessingUrl("/login") // ログイン処理を行うパス (form の action 属性と一致させる)
                .usernameParameter("userId") // ユーザー名のリクエストパラメータ名 (form の input name 属性と一致させる)
                .passwordParameter("password") // パスワードのリクエストパラメータ名 (form の input name 属性と一致させる)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}