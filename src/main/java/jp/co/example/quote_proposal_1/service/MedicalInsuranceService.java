package jp.co.example.quote_proposal_1.service; // パッケージは適切に

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.MedicalInsuranceDetailRepository;

@Service
@Transactional(readOnly = true) // 読み取り専用のトランザクション設定
public class MedicalInsuranceService {

    private final MedicalInsuranceDetailRepository medicalInsuranceDetailRepository;

    public MedicalInsuranceService(MedicalInsuranceDetailRepository medicalInsuranceDetailRepository) {
        this.medicalInsuranceDetailRepository = medicalInsuranceDetailRepository;
    }

    public List<MedicalInsuranceDetail> getMedicalRatesByProductId(Long productId) {
        return medicalInsuranceDetailRepository.findByProductIdOrderByMinAgeAsc(productId);
    }
}