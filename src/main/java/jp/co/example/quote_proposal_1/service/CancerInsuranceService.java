package jp.co.example.quote_proposal_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail; // がん保険詳細エンティティをインポート
import jp.co.example.quote_proposal_1.repository.CancerInsuranceRepository; // がん保険リポジトリをインポート

@Service
@Transactional(readOnly = true) 
public class CancerInsuranceService {

    private final CancerInsuranceRepository cancerInsuranceRepository;

    @Autowired
    public CancerInsuranceService(CancerInsuranceRepository cancerInsuranceRepository) {
        this.cancerInsuranceRepository = cancerInsuranceRepository;
    }

    public List<CancerInsuranceDetail> getCancerRatesByProductId(Long productId) {
        return cancerInsuranceRepository.findByProductIdOrderByMinAge(productId);
    }

}