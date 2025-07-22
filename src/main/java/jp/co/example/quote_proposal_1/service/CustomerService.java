package jp.co.example.quote_proposal_1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.repository.CustomerRepository; // CustomerRepositoryをインポート

@Service // SpringがこのクラスをBeanとして管理するためのアノテーション
@Transactional // トランザクション管理を行うためのアノテーション
public class CustomerService {

    private final CustomerRepository customerRepository;

    // コンストラクタインジェクションでCustomerRepositoryを注入
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * すべての顧客を取得するメソッド
     * Controllerから呼び出される
     */
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * IDで顧客を検索するメソッド
     * Controllerから呼び出される
     */
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * 顧客情報を作成または更新するメソッド
     * (EstimateControllerからも呼び出される可能性があるため、引数を充実させています)
     */
    public Customer createOrUpdateCustomer(
            Long id, String lastName, String firstName, LocalDate dateOfBirth,
            String phoneNumber, String email, Long userId) {

        Customer customer;
        if (id == null) {
            // 新規作成
            customer = new Customer();
        } else {
            // 更新
            customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("指定された顧客が見つかりません。"));
        }

        customer.setLastName(lastName);
        customer.setFirstName(firstName);
        customer.setDateOfBirth(dateOfBirth); // 生年月日を追加
        customer.setPhoneNumber(phoneNumber); // 電話番号を追加
        customer.setEmail(email); // メールアドレスを追加
        // TODO: userIdを設定するsetterが必要であれば追加（またはコンストラクタで設定）
        // customer.setCreatedBy(userRepository.findById(userId).orElse(null)); // 例: Userエンティティを紐付ける場合

        return customerRepository.save(customer);
    }

    // 必要に応じて他の顧客関連のビジネスロジックを追加
}