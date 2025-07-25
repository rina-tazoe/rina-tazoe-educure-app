package jp.co.example.quote_proposal_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;

@Repository
public interface CancerInsuranceDetailRepository extends JpaRepository<CancerInsuranceDetail, Long> {

    Optional<CancerInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);


}