package jp.co.example.quote_proposal_1.repository;

import java.util.List;
import java.util.Optional; // Optional をインポート

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;

@Repository
public interface MedicalInsuranceDetailRepository extends JpaRepository<MedicalInsuranceDetail, Long> {

    // ProductController (または製品詳細ページ) で特定の productId に紐づく全ての詳細情報を取得するメソッド
    // テーブルの min_age で昇順にソートされるようにしています
    List<MedicalInsuranceDetail> findByProductIdOrderByMinAgeAsc(Long productId);

    // 見積もり計算時に使用: productId、指定された年齢が min_age と max_age の範囲内にあるレコードを検索するメソッド
    Optional<MedicalInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(
        Long productId,
        Integer ageLessThanEqual, // age とカラム min_age の比較
        Integer ageGreaterThanEqual // age とカラム max_age の比較
    );

    // ★★★ 既存の MedicalInsuranceService に findByProductIdOrderByMinAgeAsc を既に記述している場合は
    // ★★★ findByProductId(Long productId) は不要です。
    // ★★★ もし MedicalInsuranceDetailService に findByProductId が必要な場合は、以下を追加してください。
    List<MedicalInsuranceDetail> findByProductId(Long productId);
}