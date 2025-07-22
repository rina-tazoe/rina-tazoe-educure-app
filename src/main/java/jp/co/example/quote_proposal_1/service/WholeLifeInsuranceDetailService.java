package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.WholeLifeInsuranceDetailRepository;

@Service
public class WholeLifeInsuranceDetailService {

    private final WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository;

    @Autowired
    public WholeLifeInsuranceDetailService(WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository) {
        this.wholeLifeInsuranceDetailRepository = wholeLifeInsuranceDetailRepository;
    }

    /**
     * 指定された商品IDと年齢グループに合致する終身保険詳細情報を検索します。
     * @param productId 保険商品ID
     * @param age 年齢グループ
     * @return 該当する終身保険詳細情報（Optionalでラップ）
     */
    public Optional<WholeLifeInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        // リポジトリのメソッド名に合わせて修正
        // ageをminAgeとmaxAgeの両方に渡すことで、ageが範囲内にあるかをチェック
        return wholeLifeInsuranceDetailRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }

    // 必要に応じて他のサービスメソッド...
}