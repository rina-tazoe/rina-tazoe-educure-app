// RoleRepository.java
package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.example.quote_proposal_1.entity.Role; // ★重要★

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}