package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.repository.InsuranceProductRepository;

@Service
public class InsuranceProductServiceImpl implements InsuranceProductService {

    @Autowired
    private InsuranceProductRepository insuranceProductRepository;

    @Override
    public List<InsuranceProduct> findAll() { // ★修正: findAllProducts() -> findAll() ★
        return insuranceProductRepository.findAll();
    }

    @Override
    public Optional<InsuranceProduct> findById(Long id) {
        return insuranceProductRepository.findById(id);
    }
}
