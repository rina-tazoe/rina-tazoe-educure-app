package jp.co.example.quote_proposal_1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.repository.QuoteRepository;

@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InsuranceProductService insuranceProductService;

    @Autowired
    private MedicalInsuranceDetailService medicalInsuranceDetailService;

    @Autowired
    private CancerInsuranceDetailService cancerInsuranceDetailService;

    @Autowired
    private WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService;

    @Override
    public int calculatePremium(QuoteForm quoteForm) {
        System.out.println("DEBUG: calculatePremium method called for product ID: " + quoteForm.getProductId() + ", age: " + quoteForm.getAge() + ", gender: " + quoteForm.getGender());
        return 10000; // 仮の月額保険料 (このメソッドはControllerで直接計算されるため、通常は使用されない)
    }

    @Override
    public Quote registerQuote(QuoteForm quoteForm) { // ★★★ 引数をQuoteFormのみに変更 ★★★
        System.out.println("DEBUG: registerQuote method called for QuoteForm: " + quoteForm);

        // 顧客情報の登録または取得
        Customer customer;
        Optional<Customer> existingCustomer = customerService.findByEmail(quoteForm.getEmail());

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            System.out.println("DEBUG: 既存の顧客が見つかりました。ID: " + customer.getId());
        } else {
            customer = new Customer();
            customer.setLastName(quoteForm.getLastName());
            customer.setFirstName(quoteForm.getFirstName());
            customer.setDateOfBirth(quoteForm.getDateOfBirth());
            customer.setPhoneNumber(quoteForm.getPhoneNumber());
            customer.setEmail(quoteForm.getEmail());
            // registeredByはCustomerServiceで設定される想定
            customer = customerService.saveCustomer(customer);
            System.out.println("DEBUG: 新規顧客を登録しました。ID: " + customer.getId());
        }

        // 保険商品エンティティの取得
        InsuranceProduct selectedProduct = insuranceProductService.findById(quoteForm.getProductId())
                .orElseThrow(() -> new RuntimeException("選択された保険商品が見つかりませんでした。ID: " + quoteForm.getProductId()));


        // Quoteエンティティの作成と設定
        Quote quote = new Quote();
        quote.setInsuranceProduct(selectedProduct); // InsuranceProductオブジェクトを設定
        quote.setCustomer(customer);
        quote.setAge(quoteForm.getAge());
        quote.setGender(quoteForm.getGender());

        // QuoteFormから月々金額を直接設定 (Controllerで既に計算済み)
        quote.setMonthlyPremium(quoteForm.getMonthlyPremium() != null ? quoteForm.getMonthlyPremium().doubleValue() : 0.0);
        quote.setBenefit(quoteForm.getInsuranceContent()); // 汎用的な保険内容

        // ★★★ QuoteFormから各保険タイプ固有のフィールドを直接設定 ★★★
        // QuoteFormがInteger/Double型で持つように変更したため、直接コピーできる
        quote.setDailyHospitalizationFee(quoteForm.getDailyHospitalizationFee());
        quote.setPaymentDays(quoteForm.getPaymentDays());
        quote.setBenefitAmount(quoteForm.getBenefitAmount());
        quote.setNumberOfPayments(quoteForm.getNumberOfPayments());
        quote.setSurrenderValue(quoteForm.getSurrenderValue());


        quote.setEstimateDate(LocalDate.now());
        quote.setStatus("見積もり済み");
        quote.setCreatedByUserId(1L); // 仮の値 (実際にはログインユーザーIDなどを使用)
        quote.setAmount(0); // 仮の値 (必要に応じてQuoteFormから取得)

        return quoteRepository.save(quote);
    }

    @Override
    public Optional<Quote> findById(Long id) {
        return quoteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findLatestQuoteByCustomerId(Long customerId) {
        List<Quote> quotes = quoteRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        if (!quotes.isEmpty()) {
            return Optional.of(quotes.get(0));
        }
        return Optional.empty();
    }
}