package jp.co.example.quote_proposal_1.repository;

import java.util.List; // Listをインポート

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    // ★★★ 追加: 顧客IDで検索し、作成日時で降順にソートするメソッド ★★★
    List<Quote> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
