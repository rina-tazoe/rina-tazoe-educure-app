package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.CancerInsuranceRepository; // リポジトリをインポート

@Service
@Transactional(readOnly = true)
public class CancerInsuranceDetailService { // クラス名を変更 (Detailを付与)

    private final CancerInsuranceRepository cancerInsuranceRepository;

    @Autowired
    public CancerInsuranceDetailService(CancerInsuranceRepository cancerInsuranceRepository) {
        this.cancerInsuranceRepository = cancerInsuranceRepository;
    }

    // product_id と年齢範囲に基づいてがん保険の詳細を取得
    public Optional<CancerInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        // リポジトリのメソッド名と引数に合わせて調整
        return cancerInsuranceRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }
}
