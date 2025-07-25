package jp.co.example.quote_proposal_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor; // コンストラクタの競合を避けるため追加
import lombok.Data; // Lombokを使用する場合
import lombok.NoArgsConstructor; // コンストラクタの競合を避けるため追加

@Entity
@Table(name = "roles") 
@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; 
}