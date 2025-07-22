package jp.co.example.quote_proposal_1.repository;

import java.util.List; // Listをインポート
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;

@Repository
public interface MedicalInsuranceDetailRepository extends JpaRepository<MedicalInsuranceDetail, Long> {
    // product_id と年齢範囲に基づいて医療保険の詳細を取得するメソッド
    Optional<MedicalInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    // ★★★ 追加: product_id に基づいてすべての医療保険詳細を年齢順に取得するメソッド ★★★
    List<MedicalInsuranceDetail> findByProductIdOrderByMinAgeAsc(Long productId);
}
