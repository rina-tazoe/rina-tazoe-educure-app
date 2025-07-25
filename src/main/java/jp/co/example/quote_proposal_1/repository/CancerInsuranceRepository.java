package jp.co.example.quote_proposal_1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;

@Repository
public interface CancerInsuranceRepository extends JpaRepository<CancerInsuranceDetail, Long> {
    Optional<CancerInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    List<CancerInsuranceDetail> findByProductIdOrderByMinAge(Long productId);
}