package jp.co.example.quote_proposal_1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.Estimate;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    // 顧客IDで見積もりを検索し、作成日時の降順でソート
    List<Estimate> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // 追加で必要になった場合 (以前のCustomerServiceでのdeleteAllCustomersのため)
    // void deleteByCustomerId(Long customerId); // これが必要なら追加してください。
                                              // ただし今回は単一の見積もり削除なので不要です。
                                              // JpaRepository#deleteById(ID) で十分です。
}