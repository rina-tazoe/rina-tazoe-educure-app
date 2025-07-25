package jp.co.example.quote_proposal_1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c JOIN FETCH c.registeredUser")
    List<Customer> findAllWithRegisteredUser();

    Optional<Customer> findByEmail(String email); 
}