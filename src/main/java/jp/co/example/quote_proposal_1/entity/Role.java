package jp.co.example.quote_proposal_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data; // Lombokを使用する場合

@Entity
@Table(name = "roles") // データベースのテーブル名に合わせる
@Data // Lombokのアノテーション: Getter, Setter など自動生成
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // 例: ADMIN, USER (または ROLE_ADMIN, ROLE_USER)
}