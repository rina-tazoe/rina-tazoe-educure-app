package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.CancerInsuranceRepository; // リポジトリをインポート

@Service
@Transactional(readOnly = true)
public class CancerInsuranceDetailService { 

    private final CancerInsuranceRepository cancerInsuranceRepository;

    @Autowired
    public CancerInsuranceDetailService(CancerInsuranceRepository cancerInsuranceRepository) {
        this.cancerInsuranceRepository = cancerInsuranceRepository;
    }

    public Optional<CancerInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        return cancerInsuranceRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }
}
