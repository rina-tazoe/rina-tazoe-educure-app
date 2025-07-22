package jp.co.example.quote_proposal_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.example.quote_proposal_1.entity.Customer; // ★Customerエンティティのインポートが重要★

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 必要に応じてカスタムクエリメソッドを追加
}