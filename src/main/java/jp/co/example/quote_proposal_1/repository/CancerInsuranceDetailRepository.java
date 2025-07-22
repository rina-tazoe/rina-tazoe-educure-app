package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;

@Repository
public interface CancerInsuranceDetailRepository extends JpaRepository<CancerInsuranceDetail, Long> {

    // 指定されたproductIdと、ageがminAgeとmaxAgeの範囲内にあるレコードを検索するメソッドを追加
    Optional<CancerInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    // 必要であれば、@Queryアノテーションを使ってより柔軟なクエリを定義することも可能です。
    // @Query("SELECT c FROM CancerInsuranceDetail c WHERE c.productId = :productId AND :age BETWEEN c.minAge AND c.maxAge")
    // Optional<CancerInsuranceDetail> findByProductIdAndAgeGroup(@Param("productId") Long productId, @Param("age") Integer age);

}