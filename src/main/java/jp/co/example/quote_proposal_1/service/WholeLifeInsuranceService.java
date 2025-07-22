package jp.co.example.quote_proposal_1.service; // ★このパッケージ宣言が重要です★

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;
import jp.co.example.quote_proposal_1.repository.WholeLifeInsuranceDetailRepository; // ★リポジトリ名も確認★

@Service
@Transactional(readOnly = true)
public class WholeLifeInsuranceService {

    private final WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository;

    @Autowired
    public WholeLifeInsuranceService(WholeLifeInsuranceDetailRepository wholeLifeInsuranceDetailRepository) {
        this.wholeLifeInsuranceDetailRepository = wholeLifeInsuranceDetailRepository;
    }

    // product_id に基づいて終身保険の詳細リストを取得
    public List<WholeLifeInsuranceDetail> getWholeLifeRatesByProductId(Long productId) {
        return wholeLifeInsuranceDetailRepository.findByProductIdOrderByMinAge(productId);
    }

    // 必要に応じて他のビジネスロジックを追加
}
