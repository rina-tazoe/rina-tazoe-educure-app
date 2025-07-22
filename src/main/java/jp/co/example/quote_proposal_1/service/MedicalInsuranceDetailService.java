package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.MedicalInsuranceDetailRepository; // リポジトリをインポート

@Service
@Transactional(readOnly = true)
public class MedicalInsuranceDetailService {

    private final MedicalInsuranceDetailRepository medicalInsuranceDetailRepository;

    @Autowired
    public MedicalInsuranceDetailService(MedicalInsuranceDetailRepository medicalInsuranceDetailRepository) {
        this.medicalInsuranceDetailRepository = medicalInsuranceDetailRepository;
    }

    // product_id と年齢範囲に基づいて医療保険の詳細を取得
    public Optional<MedicalInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        return medicalInsuranceDetailRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(productId, age, age);
    }
}
