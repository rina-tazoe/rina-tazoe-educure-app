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

@Entity
@Table(name = "estimates")
@Data 
@EntityListeners(AuditingEntityListener.class)
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @ManyToOne 
    @JoinColumn(name = "product_id", nullable = false) 
    private InsuranceProduct product; 

    @Column(name = "age", nullable = false)
    private Integer age; 

    @Column(name = "gender", nullable = false, length = 10)
    private String gender; 

    @Column(name = "monthly_premium", nullable = false, precision = 10, scale = 2) 
    private BigDecimal monthlyPremium; 

    @Column(name = "surrender_value", precision = 10, scale = 2) 
    private BigDecimal surrenderValue; 

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate; 

    @Column(name = "amount", nullable = false)
    private Integer amount; 

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Estimate() {
    }

    public Estimate(Customer customer, User createdBy, InsuranceProduct product, Integer age, String gender,
                    BigDecimal monthlyPremium, BigDecimal surrenderValue, LocalDate estimateDate,
                    Integer amount, String status, String description) {
        this.customer = customer;
        this.createdBy = createdBy;
        this.product = product;
        this.age = age;
        this.gender = gender;
        this.monthlyPremium = monthlyPremium;
        this.surrenderValue = surrenderValue;
        this.estimateDate = estimateDate;
        this.amount = amount;
        this.status = status;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public InsuranceProduct getProduct() { // ★ Getterも getProduct() です
        return product;
    }

    public void setProduct(InsuranceProduct product) { // ★ Setterも setProduct() です
        this.product = product;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(BigDecimal monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }

    public BigDecimal getSurrenderValue() {
        return surrenderValue;
    }

    public void setSurrenderValue(BigDecimal surrenderValue) {
        this.surrenderValue = surrenderValue;
    }

    public LocalDate getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(LocalDate estimateDate) {
        this.estimateDate = estimateDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}