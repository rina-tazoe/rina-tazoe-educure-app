package com.example.household_account_book.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.household_account_book.entity.CustomUserDetails;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("loadUserByUsername called with email: {}", email); // 入力されたメールアドレスを出力

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User found: {}", user); // 取得したユーザー情報を出力
            // ★ 削除されたユーザーは認証させない ★
            if (user.getDeletedAt() != null) {
                logger.warn("User {} is退会済み", email);
                throw new UsernameNotFoundException("ユーザー " + email + " は退会済みです。");
            }
            CustomUserDetails userDetails = new CustomUserDetails(user);
            logger.info("UserDetails created: {}", userDetails); // 生成した UserDetails を出力
            return userDetails;
        } else {
            logger.warn("User not found with email: {}", email); // ユーザーが見つからなかった場合の警告
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}