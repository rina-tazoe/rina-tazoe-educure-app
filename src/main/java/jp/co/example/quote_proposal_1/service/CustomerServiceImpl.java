package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.repository.CustomerRepository;
import jp.co.example.quote_proposal_1.repository.UserRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return customerRepository.findAllWithRegisteredUser();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            Long currentUserId = getCurrentAuthenticatedUserId();
            if (currentUserId != null) {
                userRepository.findById(currentUserId).ifPresent(customer::setRegisteredUser);
            } else {
                System.err.println("WARN: Could not get authenticated user ID for new customer registration.");
            }
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override 
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    private Long getCurrentAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    return user.get().getId();
                } else {
                    System.err.println("ERROR: User not found in DB for username: " + username);
                    return null;
                }
            }
        }
        return null;
    }
}