package com.example.household_account_book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext // ★ EntityManager を注入 ★
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
    public void deleteUser(Integer id) { // ★ 管理者用: ID で削除 ★
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUser(String username) { // ★ 一般ユーザー用: ユーザー名で削除 ★
        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.ifPresent(user -> {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        });
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}