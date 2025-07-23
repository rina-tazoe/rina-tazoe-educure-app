package jp.co.example.quote_proposal_1.entity;

import java.math.BigDecimal; // BigDecimal を使用するため追加
import java.time.LocalDate; // LocalDate を使用するため追加
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

import lombok.Data; // Lombok を使用している場合

@Entity
@Table(name = "estimates")
@Data // Lombok を使用している場合、getter/setter/toStringなどが自動生成されます
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

    @ManyToOne // 保険商品との多対一リレーション
    @JoinColumn(name = "product_id", nullable = false) // データベースの外部キーカラム名
    private InsuranceProduct product; // ★ここが "product" です

    @Column(name = "age", nullable = false)
    private Integer age; // 顧客の年齢（見積もり時点）

    @Column(name = "gender", nullable = false, length = 10)
    private String gender; // 顧客の性別（見積もり時点）

    @Column(name = "monthly_premium", nullable = false, precision = 10, scale = 2) // 例: 10桁の整数部、2桁の小数部
    private BigDecimal monthlyPremium; // 月払い保険料

    @Column(name = "surrender_value", precision = 10, scale = 2) // 返戻金（Nullableの場合もある）
    private BigDecimal surrenderValue; // 解約返戻金

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate; // 見積もり日

    @Column(name = "amount", nullable = false)
    private Integer amount; // (Note: monthlyPremiumと重複する可能性あり。どちらか一方に統一を検討)

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

    // デフォルトコンストラクタ (JPAが必要とする)
    public Estimate() {
    }

    // 全てのフィールドを引数に取るコンストラクタ（Lombokの@Dataがあれば通常不要だが、テストや初期化に便利）
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

    // Lombok の @Data アノテーションがあれば、以下の getter/setter は自動生成されますが、
    // 明示的に記述する場合は以下のようになります。

    // GetterとSetter
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
