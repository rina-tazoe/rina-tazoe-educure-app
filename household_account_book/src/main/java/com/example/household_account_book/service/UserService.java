package com.example.household_account_book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.household_account_book.entity.User;
import com.example.household_account_book.repository.RoleRepository;
import com.example.household_account_book.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User registerUser(String userName, String email, String password) {
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setEmail(email);
        newUser.setPassword(password); // TODO: パスワードのハッシュ化
        roleRepository.findByRoleName("一般").ifPresent(newUser::setRole);
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}