package com.example.household_account_book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.household_account_book.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // ★ CustomUserDetailsService を注入 ★

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // ★ 本番環境ではPasswordEncoderを変更してください ★
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // ★ 認証プロバイダーを定義 ★
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider()) // ★ 認証プロバイダーを設定 ★
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/home"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/reset-password")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin/category/add")).permitAll() // ★ GET /admin/category/add を許可 ★
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("管理者")
                .requestMatchers(new AntPathRequestMatcher("/categories/**")).hasAuthority("管理者")
                .requestMatchers(new AntPathRequestMatcher("/expenses/list")).hasAnyAuthority("一般", "管理者")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email") // ★ フォームのユーザー名パラメータを "email" に変更 ★
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
            )
            .exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}