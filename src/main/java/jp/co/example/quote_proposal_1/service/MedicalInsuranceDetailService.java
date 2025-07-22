package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.MedicalInsuranceDetailRepository;

@Service
public class MedicalInsuranceDetailService {

    private final MedicalInsuranceDetailRepository medicalInsuranceDetailRepository;

    @Autowired
    public MedicalInsuranceDetailService(MedicalInsuranceDetailRepository medicalInsuranceDetailRepository) {
        this.medicalInsuranceDetailRepository = medicalInsuranceDetailRepository;
    }

    /**
     * 指定された商品IDと年齢に合致する医療保険詳細情報を検索します。
     * 年齢はmin_ageとmax_ageの範囲内で検索されます。
     * @param productId 保険商品ID
     * @param age 検索対象の年齢
     * @return 該当する医療保険詳細情報（Optionalでラップ）
     */
    // ★★★ このメソッドを以下のように修正してください ★★★
    public Optional<MedicalInsuranceDetail> findByProductIdAndAgeGroup(Long productId, Integer age) {
        // リポジトリの findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual メソッドを呼び出します。
        // 引数の age は min_age と max_age の両方に渡すことで、指定された年齢がその範囲内にあるレコードを検索します。
        return medicalInsuranceDetailRepository.findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(
            productId,
            age, // age は min_ageLessThanEqual に対応
            age  // age は max_ageGreaterThanEqual に対応
        );
    }

    // 必要に応じて他のサービスメソッド...
    // 例: 全ての医療保険詳細を取得するメソッド (QuoteControllerの showQuoteForm からは呼び出していませんが、もし必要なら)
    // public List<MedicalInsuranceDetail> findAll() {
    //     return medicalInsuranceDetailRepository.findAll();
    // }

    // 特定のproductIdに紐づく全ての詳細情報を取得するメソッド (リポジトリに定義されているメソッドをラップ)
    public List<MedicalInsuranceDetail> findByProductIdOrderByMinAgeAsc(Long productId) {
        return medicalInsuranceDetailRepository.findByProductIdOrderByMinAgeAsc(productId);
    }
}