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

import lombok.Data; // Lombokを使用している場合

@Entity
@Table(name = "cancer_insurance_details") // DBのテーブル名に合わせて
@Data // Lombokのアノテーションでgetter/setterなどを自動生成
@EntityListeners(AuditingEntityListener.class) // 作成日時・更新日時自動設定用
public class CancerInsuranceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancer_insurance_detail_id") // DBのカラム名に合わせる
    private Long cancerInsuranceDetailId; // フィールド名をDBのカラム名と合わせるか、@Columnでマッピング

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

    @Column(name = "benefit_amount") // data.sqlでVARCHARになっているのでStringに
    private String benefitAmount;

    @Column(name = "payment_frequency") // data.sqlでVARCHARになっているのでStringに
    private String paymentFrequency;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // デフォルトコンストラクタ (JPAが必要とする)
    public CancerInsuranceDetail() {
    }

    // Lombokの@Dataアノテーションがあれば、以下のgetter/setterは不要ですが、
    // 明示的に記述する場合は含めてください。
    // public Long getCancerInsuranceDetailId() { return cancerInsuranceDetailId; }
    // public void setCancerInsuranceDetailId(Long cancerInsuranceDetailId) { this.cancerInsuranceDetailId = cancerInsuranceDetailId; }
    // (他のフィールドも同様)
}
