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
@Table(name = "insurance_products")
@Data
@EntityListeners(AuditingEntityListener.class)
public class InsuranceProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName; // HTMLの estimate.insuranceProduct?.name に対応

    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription; // HTMLの estimate.insuranceProduct?.description に対応

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public InsuranceProduct() {
    }

    // 必要であれば、nameのゲッターを追加してHTMLの.name参照に対応させる
    public String getName() {
        return this.productName;
    }

    // 必要であれば、descriptionのゲッターを追加してHTMLの.description参照に対応させる
    public String getDescription() {
        return this.productDescription;
    }
}