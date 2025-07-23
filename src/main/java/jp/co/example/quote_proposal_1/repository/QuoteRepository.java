package jp.co.example.quote_proposal_1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    // 顧客IDと作成日時（新しい順）で取得するメソッド
    List<Quote> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // ★追加: 見積もり日付（新しい順）で取得するメソッド
    List<Quote> findByCustomerIdOrderByEstimateDateDesc(Long customerId);

    // その他、必要に応じて追加
    // Optional<Quote> findByIdAndCustomerId(Long id, Long customerId);
}