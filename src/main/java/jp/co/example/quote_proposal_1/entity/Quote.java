package jp.co.example.quote_proposal_1.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
@Table(name = "estimates") // テーブル名はestimatesのまま
@Data
@EntityListeners(AuditingEntityListener.class)
public class Quote { // エンティティ名はQuoteのまま

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // HTMLが estimate.insuranceProduct?.name を参照するため、InsuranceProductエンティティへの関連を追加
    @ManyToOne(fetch = FetchType.EAGER) // 保険商品情報を取得時に即時ロード
    @JoinColumn(name = "product_id", nullable = false) // estimatesテーブルのproduct_idカラムと紐付け
    private InsuranceProduct insuranceProduct; // 保険商品エンティティ

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "monthly_premium", nullable = false)
    private Double monthlyPremium; // 月々金額はDouble型で保持

    @Column(name = "benefit") // 汎用的な保険内容説明 (HTMLのunlessブロックで使われる想定)
    private String benefit;

    // HTMLで参照されている各保険タイプ固有のフィールドを追加
    @Column(name = "daily_hospitalization_fee") // 医療保険用
    private Integer dailyHospitalizationFee;

    @Column(name = "payment_days") // 医療保険用
    private Integer paymentDays;

    @Column(name = "benefit_amount") // がん保険・終身保険の給付金額用 (HTMLの#numbers.formatIntegerで表示されるためInteger)
    private Integer benefitAmount;

    @Column(name = "number_of_payments") // がん保険の支払い回数用
    private Integer numberOfPayments;

    @Column(name = "surrender_value") // 終身保険の解約返戻金用
    private Double surrenderValue; // 解約返戻金はDouble型で保持

    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    @Column(name = "amount") // 仮のフィールド、用途に応じて
    private Integer amount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Quote() {
        this.estimateDate = LocalDate.now();
        this.status = "見積もり済み";
    }
}