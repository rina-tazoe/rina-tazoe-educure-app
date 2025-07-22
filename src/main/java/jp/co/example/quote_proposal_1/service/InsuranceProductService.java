package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;

public interface InsuranceProductService {
    List<InsuranceProduct> findAll(); // ★修正: findAllProducts() -> findAll() ★
    Optional<InsuranceProduct> findById(Long id);
}
