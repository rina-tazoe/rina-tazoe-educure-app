package jp.co.example.quote_proposal_1.repository;

import java.util.List; // Listをインポート
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;

@Repository
public interface CancerInsuranceRepository extends JpaRepository<CancerInsuranceDetail, Long> {
    // product_id と年齢範囲に基づいてがん保険の詳細を取得するメソッド (QuoteControllerの計算用)
    Optional<CancerInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    // ★★★ 追加: product_id に基づいてすべてのがん保険詳細を年齢順に取得するメソッド (ProductControllerの詳細ページ用) ★★★
    List<CancerInsuranceDetail> findByProductIdOrderByMinAge(Long productId);
}
