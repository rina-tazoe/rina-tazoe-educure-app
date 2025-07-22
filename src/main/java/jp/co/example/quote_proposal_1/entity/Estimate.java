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

    // ★★★ ここから追加するフィールド ★★★

    @ManyToOne // 保険商品との多対一リレーション
    @JoinColumn(name = "product_id", nullable = false) // データベースの外部キーカラム名
    private InsuranceProduct product; // 選択された保険商品

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
    // ★★★ ここまで追加するフィールド ★★★

    // 既存のフィールド
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
}