package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.repository.InsuranceProductRepository;

@Service
@Transactional
public class ProductService {

    private final InsuranceProductRepository productRepository;

    @Autowired
    public ProductService(InsuranceProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<InsuranceProduct> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<InsuranceProduct> findProductById(Long id) {
        return productRepository.findById(id);
    }

    // findByName ではなく findByProductName を呼び出すように修正
    public Optional<InsuranceProduct> findProductByName(String name) {
        return productRepository.findByProductName(name); // ★ここを修正してください
    }
}