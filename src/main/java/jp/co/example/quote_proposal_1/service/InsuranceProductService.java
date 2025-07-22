package jp.co.example.quote_proposal_1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.repository.InsuranceProductRepository; // リポジトリをインポート

@Service // Springのサービスコンポーネントとして登録
public class InsuranceProductService {

    private final InsuranceProductRepository insuranceProductRepository; // リポジトリを注入

    // コンストラクタインジェクション
    public InsuranceProductService(InsuranceProductRepository insuranceProductRepository) {
        this.insuranceProductRepository = insuranceProductRepository;
    }

    public List<InsuranceProduct> findAllProducts() {
        // リポジトリを使って全ての保険商品を取得
        return insuranceProductRepository.findAll();
    }

    // 必要に応じて、IDで商品を取得するメソッドなどを追加できます
    public InsuranceProduct findProductById(Long id) {
        return insuranceProductRepository.findById(id).orElse(null);
    }
}