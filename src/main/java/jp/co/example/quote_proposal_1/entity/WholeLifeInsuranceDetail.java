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
@Table(name = "whole_life_insurance_details")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WholeLifeInsuranceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wholeLifeInsuranceDetailId; // IDフィールド名はプロジェクトに合わせて調整してください

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

    @Column(name = "benefit_amount", nullable = false)
    private Integer benefitAmount; // ★ここを String から Integer に変更済みであることを確認★

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}