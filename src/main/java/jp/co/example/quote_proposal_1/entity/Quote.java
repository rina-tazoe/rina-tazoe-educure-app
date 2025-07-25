package jp.co.example.quote_proposal_1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotes") 
@Data
@NoArgsConstructor 
@EntityListeners(AuditingEntityListener.class)
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "insurance_product_id", nullable = false) 
    private InsuranceProduct insuranceProduct;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "monthly_premium", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPremium;

    @Column(name = "benefit", columnDefinition = "TEXT") 
    private String benefit;

    @Column(name = "daily_hospitalization_fee", precision = 10, scale = 2)
    private BigDecimal dailyHospitalizationFee; 

    @Column(name = "payment_days")
    private Integer paymentDays;

    @Column(name = "benefit_amount", precision = 10, scale = 2)
    private BigDecimal benefitAmount; 

    @Column(name = "number_of_payments")
    private Integer numberOfPayments;

    @Column(name = "surrender_value", precision = 10, scale = 2)
    private BigDecimal surrenderValue;

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_by_user_id") 
    private Long createdByUserId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}