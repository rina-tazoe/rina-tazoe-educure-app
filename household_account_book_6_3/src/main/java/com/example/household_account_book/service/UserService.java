package com.example.household_account_book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_account_book.entity.Role;
import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.RoleRepository;
import com.example.household_account_book.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role defaultRole = roleRepository.findByRoleName("一般")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAllWithRole();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByUserName(String username) {
        logger.info("ユーザー名による退会処理を開始します。ユーザー名: {}", username);
        Query query = entityManager.createQuery("DELETE FROM User u WHERE u.userName = :userName");
        query.setParameter("userName", username);
        int deletedCount = query.executeUpdate();
        logger.info("EntityManager で {} 件のユーザーを削除しようとしました。", deletedCount);
    }

    @Transactional
    public void deleteUserByEmail(String email) { // メールアドレスで削除するメソッドを追加
        logger.info("メールアドレスによる退会処理を開始します。メールアドレス: {}", email);
        userRepository.deleteByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}