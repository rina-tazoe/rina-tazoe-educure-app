package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {

    // InsuranceProduct エンティティの 'productName' フィールドを使って検索するメソッドに修正
    // メソッド名を findByProductName に変更
    Optional<InsuranceProduct> findByProductName(String productName); // ★ここを修正してください
}