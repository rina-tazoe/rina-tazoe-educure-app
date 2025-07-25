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
@Table(name = "medical_insurance_details") 
@Data
@EntityListeners(AuditingEntityListener.class)
public class MedicalInsuranceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_insurance_detail_id") 
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "min_age", nullable = false)
    private Integer minAge;

    @Column(name = "max_age", nullable = false)
    private Integer maxAge;

    @Column(name = "premium_male", nullable = false)
    private Integer paymentMonthlyMale; 

    @Column(name = "premium_female", nullable = false)
    private Integer paymentMonthlyFemale; 

    @Column(name = "daily_hospitalization_benefit", nullable = false)
    private Integer dailyHospitalizationBenefit;

    @Column(name = "payment_days", nullable = false)
    private Integer paymentDays;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public MedicalInsuranceDetail() {
    }
}