package jp.co.example.quote_proposal_1.repository;

import java.util.List; // Listをインポート
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;

@Repository
public interface WholeLifeInsuranceDetailRepository extends JpaRepository<WholeLifeInsuranceDetail, Long> {

    // 指定されたproductIdと、ageがminAgeとmaxAgeの範囲内にあるレコードを検索するメソッド
    Optional<WholeLifeInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    // ★★★ 追加: product_id に基づいて終身保険の詳細リストを取得し、minAgeでソートするメソッド ★★★
    List<WholeLifeInsuranceDetail> findByProductIdOrderByMinAge(Long productId);

    // 必要であれば、@Queryアノテーションを使ってより柔軟なクエリを定義することも可能です。
    // @Query("SELECT w FROM WholeLifeInsuranceDetail w WHERE w.productId = :productId AND :age BETWEEN w.minAge AND w.maxAge")
    // Optional<WholeLifeInsuranceDetail> findByProductIdAndAgeGroup(@Param("productId") Long productId, @Param("age") Integer age);
}
