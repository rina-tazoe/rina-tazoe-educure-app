package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.WholeLifeInsuranceDetailRepository; // リポジトリをインポート

@Service
@Transactional(readOnly = true)
public class WholeLifeInsuranceDetailService { 

    private final WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository;

    @Autowired
    public WholeLifeInsuranceDetailService(WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository) {
        this.wholeLifeInsuranceDetailRepository = wholeLifeInsuranceDetailRepository;
    }

    // product_id と年齢範囲で終身保険を取得
    public Optional<WholeLifeInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        return wholeLifeInsuranceDetailRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }
}
