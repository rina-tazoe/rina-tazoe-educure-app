// UserRepository.java
package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.example.quote_proposal_1.entity.User; // ★重要★

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}