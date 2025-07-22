package jp.co.example.quote_proposal_1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 日時自動更新のためのアノテーション
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity // このクラスがJPAエンティティであることを示す
@Table(name = "users") // データベースのテーブル名を指定
@Data // Lombokのアノテーション: Getter, Setter, toString, equals, hashCode を自動生成
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate を有効にする
public class User {

    @Id // プライマリキーを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // データベースのIDENTITY列 (例: PostgreSQLのSERIAL) にマッピング
    @Column(name = "user_id") // DBカラム名を明示的に指定
    private Long id; // ユーザーID

    @Column(name = "username", nullable = false, unique = true, length = 50) // ユーザー名、NULL不可、ユニーク、最大長50
    private String username;

    @Column(name = "password", nullable = false, length = 255) // パスワード（ハッシュ化されるため長め）
    private String password;

    // ★★★ ここから追加 ★★★
    @Column(name = "role_id", nullable = false) // ロールIDカラム
    private Long roleId; // ユーザーのロールID
    // ★★★ ここまで追加 ★★★

    @CreatedDate // エンティティが最初に永続化されるときに自動的に現在日時を設定
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // エンティティが更新されるたびに自動的に現在日時を設定
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // デフォルトコンストラクタ (JPAが必要とする)
    public User() {
    }

    // 必要に応じて、初期値を設定するためのコンストラクタを更新
    public User(String username, String password, Long roleId) { // roleIdを追加
        this.username = username;
        this.password = password;
        this.roleId = roleId; // roleIdを設定
    }
}