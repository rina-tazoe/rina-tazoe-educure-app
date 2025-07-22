package jp.co.example.quote_proposal_1.service;

import java.math.BigDecimal;
import java.time.LocalDate; // 必要であれば
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer; // 必要であれば
import jp.co.example.quote_proposal_1.entity.Estimate;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct; // 必要であれば
import jp.co.example.quote_proposal_1.entity.Premium; // 見積もり計算結果用DTO/Entityを仮定
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.repository.CustomerRepository; // 必要であれば
import jp.co.example.quote_proposal_1.repository.EstimateRepository;
import jp.co.example.quote_proposal_1.repository.InsuranceProductRepository; // 必要であれば
import jp.co.example.quote_proposal_1.repository.UserRepository; // 必要であれば

@Service // SpringがこのクラスをBeanとして管理するためのアノテーション
@Transactional // トランザクション管理を行うためのアノテーション
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final CustomerRepository customerRepository; // 関連エンティティ操作のため
    private final InsuranceProductRepository insuranceProductRepository; // 関連エンティティ操作のため
    private final UserRepository userRepository; // 関連エンティティ操作のため

    // コンストラクタインジェクション
    @Autowired
    public EstimateService(EstimateRepository estimateRepository,
                           CustomerRepository customerRepository,
                           InsuranceProductRepository insuranceProductRepository,
                           UserRepository userRepository) {
        this.estimateRepository = estimateRepository;
        this.customerRepository = customerRepository;
        this.insuranceProductRepository = insuranceProductRepository;
        this.userRepository = userRepository;
    }

    /**
     * IDで見積もりを検索するメソッド
     * Controllerから呼び出される
     */
    public Optional<Estimate> findEstimateById(Long id) {
        return estimateRepository.findById(id);
    }

    /**
     * 顧客IDに関連する最新の見積もりを取得するメソッド (CustomerControllerから呼び出される)
     * これは仮のメソッドなので、必要に応じてクエリを調整してください。
     */
    public Optional<Estimate> findLatestEstimateByCustomerId(Long customerId) {
        // 例: Customer IDで検索し、登録日などで最新のものを取得するクエリをリポジトリに追加する必要がある
        // または、Customerエンティティに最新の見積もりへのリレーションがあればそれを利用
        List<Estimate> estimates = estimateRepository.findByCustomerIdOrderByCreatedAtDesc(customerId); // 仮定
        if (!estimates.isEmpty()) {
            return Optional.of(estimates.get(0));
        }
        return Optional.empty();
    }

    /**
     * 見積もりを計算する（ビジネスロジック）
     * ここに年齢、性別、商品に応じた保険料計算ロジックを実装
     */
    public Premium getCalculatedPremium(Long productId, int age, String gender) {
        // TODO: 実際の保険料計算ロジックをここに実装する
        // 例: データベースの料金表から取得、複雑な計算を行うなど
        // 現時点では仮の値を返す
        BigDecimal monthly = BigDecimal.valueOf(10000 + (age * 100) + (gender.equals("男性") ? 500 : 0));
        BigDecimal surrender = monthly.multiply(BigDecimal.valueOf(12)).multiply(BigDecimal.valueOf(0.5)); // 適当な返戻金

        return new Premium(monthly, surrender);
    }

    /**
     * 見積もりをデータベースに保存するメソッド
     */
    public Estimate saveEstimate(Long customerId, Long userId, Long productId,
                                  Integer age, String gender, BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("指定された顧客が見つかりません。"));
        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された保険商品が見つかりません。"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("登録ユーザーが見つかりません。"));

        Estimate estimate = new Estimate();
        estimate.setCustomer(customer);
        estimate.setCreatedBy(user); // 見積もりを作成したユーザー
        estimate.setProduct(product);
        estimate.setAge(age);
        estimate.setGender(gender);
        estimate.setMonthlyPremium(monthlyPremium);
        estimate.setSurrenderValue(surrenderValue);
        estimate.setEstimateDate(LocalDate.now()); // 今日の日付を設定

        return estimateRepository.save(estimate);
    }

    /**
     * 既存の見積もりを更新するメソッド
     */
    public Estimate updateEstimate(Long id, Long productId, Integer age, String gender,
                                    BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("更新対象の見積もりが見つかりません。"));

        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された保険商品が見つかりません。"));

        estimate.setProduct(product);
        estimate.setAge(age);
        estimate.setGender(gender);
        estimate.setMonthlyPremium(monthlyPremium);
        estimate.setSurrenderValue(surrenderValue);
        // 更新日なども必要であれば設定

        return estimateRepository.save(estimate);
    }

    // 必要に応じて他の見積もり関連のビジネスロジックを追加
}