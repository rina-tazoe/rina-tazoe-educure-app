package jp.co.example.quote_proposal_1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.example.quote_proposal_1.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    // 特定の顧客IDに関連する全ての見積もりを検索し、作成日時の新しい順に並べる
    // `createdAt` が `Estimate` エンティティに正しく定義されていることを前提とする
    List<Estimate> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // 必要に応じて、他のカスタムクエリメソッドを追加できます

    // 例: 特定のステータスの見積もりを検索
    // List<Estimate> findByStatus(String status);

    // 例: 特定の顧客IDとステータスの見積もりを検索
    // List<Estimate> findByCustomerIdAndStatus(Long customerId, String status);

    // 例: IDで検索し、OptionalでラップしてNotFound時のハンドリングを容易にする
    // Optional<Estimate> findById(Long id); // JpaRepositoryにデフォルトで含まれている
}