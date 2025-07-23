package jp.co.example.quote_proposal_1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users") // テーブル名がusersであることを明示
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ★★★ 修正: ID生成戦略をIDENTITYに設定 ★★★
    @Column(name = "user_id") // DBのカラム名がuser_idであることを明示
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) // Roleエンティティへの関連付け
    @JoinColumn(name = "role_id", nullable = false) // userテーブルのrole_idカラムと紐付け
    private Role role; // Roleオブジェクトを持つ

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // PrePersistとPreUpdateは引き続き必要
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}