package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional; // Optional をインポート

import jp.co.example.quote_proposal_1.entity.Customer;

public interface CustomerService {
    List<Customer> findAllCustomers();
    Customer saveCustomer(Customer customer);
    Optional<Customer> findByEmail(String email); // 既存
    Optional<Customer> findById(Long id); // 既存
    
    // ★追加: CustomerService インターフェースに findCustomerById メソッドを定義
    Customer findCustomerById(Long id);
}