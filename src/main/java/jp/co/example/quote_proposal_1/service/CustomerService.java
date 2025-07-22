package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 顧客を保存または更新する
    public Customer saveCustomer(Customer customer) {
        // ★★★ registeredBy に仮の値を設定します ★★★
        // 実際には、認証されたユーザーのIDを設定する必要があります。
        // 現時点ではユーザー認証機能がないため、仮の値として1Lを設定します。
        if (customer.getRegisteredBy() == null) {
            customer.setRegisteredBy(1L); // 仮のユーザーID
        }
        return customerRepository.save(customer);
    }

    // メールアドレスで顧客を検索する
    @Transactional(readOnly = true)
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // IDで顧客を検索する
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    // すべての顧客を取得するメソッド
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
