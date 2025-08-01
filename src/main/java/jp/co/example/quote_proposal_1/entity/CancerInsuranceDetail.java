package jp.co.example.quote_proposal_1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Table(name = "cancer_insurance_details")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CancerInsuranceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancerInsuranceDetailId; 

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "min_age", nullable = false)
    private Integer minAge;

    @Column(name = "max_age", nullable = false)
    private Integer maxAge;

    @Column(name = "payment_monthly_male", nullable = false)
    private Integer paymentMonthlyMale;

    @Column(name = "payment_monthly_female", nullable = false)
    private Integer paymentMonthlyFemale;

    @Column(name = "benefit_amount", nullable = false)
    private Integer benefitAmount;

    @Column(name = "payment_frequency", nullable = false)
    private Integer paymentFrequency;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
