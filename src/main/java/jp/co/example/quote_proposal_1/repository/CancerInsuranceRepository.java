package jp.co.example.quote_proposal_1.repository; // ★このパッケージ宣言が重要です★

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail; // 作成したエンティティをインポート

@Repository
public interface CancerInsuranceRepository extends JpaRepository<CancerInsuranceDetail, Long> {
    // product_id に基づいてがん保険の詳細を取得するメソッド
    List<CancerInsuranceDetail> findByProductIdOrderByMinAge(Long productId);
}
