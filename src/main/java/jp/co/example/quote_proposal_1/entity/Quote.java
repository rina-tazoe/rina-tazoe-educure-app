package jp.co.example.quote_proposal_1.entity;

import java.math.BigDecimal; // BigDecimal を使用するため追加
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
@Table(name = "quotes") // テーブル名が quotes であると仮定
@Data
@NoArgsConstructor // JPAのために引数なしコンストラクタが必要
@EntityListeners(AuditingEntityListener.class)
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "insurance_product_id", nullable = false) // データベースのカラム名に合わせる
    private InsuranceProduct insuranceProduct;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "monthly_premium", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPremium; // ★型をBigDecimalに変更

    @Column(name = "benefit", columnDefinition = "TEXT") // 保険内容/特約内容
    private String benefit;

    @Column(name = "daily_hospitalization_fee", precision = 10, scale = 2)
    private BigDecimal dailyHospitalizationFee; // ★型をBigDecimalに変更

    @Column(name = "payment_days")
    private Integer paymentDays;

    @Column(name = "benefit_amount", precision = 10, scale = 2)
    private BigDecimal benefitAmount; // ★型をBigDecimalに変更

    @Column(name = "number_of_payments")
    private Integer numberOfPayments;

    @Column(name = "surrender_value", precision = 10, scale = 2)
    private BigDecimal surrenderValue; // ★型をBigDecimalに変更

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_by_user_id") // 登録ユーザーのIDを直接持つ場合
    private Long createdByUserId;

    @Column(name = "amount") // これは以前の `amount` フィールド。必要に応じて削除または用途を明確にする
    private Integer amount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // GetterとSetterはLombokの@Dataアノテーションによって自動生成されます。
    // 手動で記述する場合は以下のようにします（例: monthlyPremium）
    /*
    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(BigDecimal monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }
    */
}
