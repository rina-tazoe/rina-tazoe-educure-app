package jp.co.example.quote_proposal_1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;

@Repository
public interface WholeLifeInsuranceDetailRepository extends JpaRepository<WholeLifeInsuranceDetail, Long> {

    Optional<WholeLifeInsuranceDetail> findByProductIdAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(Long productId, Integer ageMin, Integer ageMax);

    List<WholeLifeInsuranceDetail> findByProductIdOrderByMinAge(Long productId);

}
