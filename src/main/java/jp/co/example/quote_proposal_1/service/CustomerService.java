package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional; // Optional をインポート

import jp.co.example.quote_proposal_1.entity.Customer;

public interface CustomerService {
    List<Customer> findAllCustomers();
    Customer saveCustomer(Customer customer);
    Optional<Customer> findByEmail(String email); 
    Optional<Customer> findById(Long id); 
    
    Customer findCustomerById(Long id);
}