package jp.co.example.quote_proposal_1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Quote> findByCustomerIdOrderByEstimateDateDesc(Long customerId);

}