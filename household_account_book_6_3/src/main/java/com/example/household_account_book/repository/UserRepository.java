package com.example.household_account_book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.transaction.annotation.Transactional; // この行を削除

import com.example.household_account_book.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role")
    List<User> findAllWithRole();

    @Modifying
    @Query("DELETE FROM User u WHERE u.userName = :userName")
    void deleteByUserName(@Param("userName") String userName);
}