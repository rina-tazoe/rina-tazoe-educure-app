package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {

    Optional<InsuranceProduct> findByProductName(String productName); 
}