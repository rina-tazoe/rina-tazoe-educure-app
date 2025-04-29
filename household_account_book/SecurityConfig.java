package com.example.household_account_book;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/home"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/reset-password")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users/**")).hasAuthority("管理者")
                .requestMatchers(new AntPathRequestMatcher("/categories/**")).hasAuthority("管理者")
                .requestMatchers(new AntPathRequestMatcher("/expenses/list")).hasAnyAuthority("一般", "管理者")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("userId") // ★ ログインフォームの name 属性が "userId" であることを確認 ★
                .passwordParameter("password")
                .defaultSuccessUrl("/main", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
            )
            .exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedPage("/access-denied") // ★ アクセス拒否時のリダイレクト先 ★
            );

        return http.build();
    }
}