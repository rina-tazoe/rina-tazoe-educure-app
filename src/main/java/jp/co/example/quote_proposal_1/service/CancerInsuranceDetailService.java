package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.CancerInsuranceDetailRepository;

@Service
public class CancerInsuranceDetailService {

    private final CancerInsuranceDetailRepository cancerInsuranceDetailRepository;

    @Autowired
    public CancerInsuranceDetailService(CancerInsuranceDetailRepository cancerInsuranceDetailRepository) {
        this.cancerInsuranceDetailRepository = cancerInsuranceDetailRepository;
    }

    /**
     * 指定された商品IDと年齢グループに合致するがん保険詳細情報を検索します。
     * @param productId 保険商品ID
     * @param age 年齢グループ
     * @return 該当するがん保険詳細情報（Optionalでラップ）
     */
    public Optional<CancerInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        // リポジトリのメソッド名に合わせて修正
        // ageをminAgeとmaxAgeの両方に渡すことで、ageが範囲内にあるかをチェック
        return cancerInsuranceDetailRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }

    // 必要に応じて他のサービスメソッド...
}