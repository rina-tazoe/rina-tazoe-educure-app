package jp.co.example.quote_proposal_1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.repository.QuoteRepository;
import jp.co.example.quote_proposal_1.repository.UserRepository;

// 不要なインポートを削除 (MedicalInsuranceDetailServiceなどはQuoteServiceImplでは直接使用しないため)
// import jp.co.example.quote_proposal_1.service.MedicalInsuranceDetailService;
// import jp.co.example.quote_proposal_1.service.CancerInsuranceDetailService;
// import jp.co.example.quote_proposal_1.service.WholeLifeInsuranceDetailService;

@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InsuranceProductService insuranceProductService;

    // 不要なサービスは削除
    // @Autowired
    // private MedicalInsuranceDetailService medicalInsuranceDetailService;
    // @Autowired
    // private CancerInsuranceDetailService cancerInsuranceDetailService;
    // @Autowired
    // private WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public int calculatePremium(QuoteForm quoteForm) {
        System.out.println("DEBUG: calculatePremium method called for product ID: " + quoteForm.getProductId() + ", age: " + quoteForm.getAge() + ", gender: " + quoteForm.getGender());
        return 10000; // 仮の月額保険料 (このメソッドはControllerで直接計算されるため、通常は使用されない)
    }

    @Override
    public Quote registerQuote(QuoteForm quoteForm) {
        System.out.println("DEBUG: registerQuote method called for QuoteForm: " + quoteForm);

        // 顧客情報の登録または取得
        Customer customer;
        Optional<Customer> existingCustomer = customerService.findByEmail(quoteForm.getEmail());

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            // 既存顧客が見つかった場合、最新の情報で更新
            customer.setLastName(quoteForm.getLastName());
            customer.setFirstName(quoteForm.getFirstName());
            customer.setDateOfBirth(quoteForm.getDateOfBirth());
            customer.setPhoneNumber(quoteForm.getPhoneNumber());
            // emailは検索キーなので更新不要
            customer = customerService.saveCustomer(customer); // 更新を保存
            System.out.println("DEBUG: 既存の顧客情報を更新しました。ID: " + customer.getId() + ", Email: " + customer.getEmail());
        } else {
            customer = new Customer();
            customer.setLastName(quoteForm.getLastName());
            customer.setFirstName(quoteForm.getFirstName());
            customer.setDateOfBirth(quoteForm.getDateOfBirth());
            customer.setPhoneNumber(quoteForm.getPhoneNumber());
            customer.setEmail(quoteForm.getEmail());
            customer = customerService.saveCustomer(customer); // 新規顧客を保存
            System.out.println("DEBUG: 新規顧客を登録しました。ID: " + customer.getId() + ", Email: " + customer.getEmail());
        }

        // 保険商品エンティティの取得
        InsuranceProduct selectedProduct = insuranceProductService.findById(quoteForm.getProductId())
                .orElseThrow(() -> new RuntimeException("選択された保険商品が見つかりませんでした。ID: " + quoteForm.getProductId()));

        // Quoteエンティティの作成と設定
        Quote quote = new Quote();
        quote.setInsuranceProduct(selectedProduct);
        quote.setCustomer(customer); // 更新または新規登録された顧客をセット
        quote.setAge(quoteForm.getAge());
        quote.setGender(quoteForm.getGender());

        // QuoteFormから月々金額を直接設定 (Controllerで既に計算済み)
        // ★★★ 修正箇所: QuoteFormのBigDecimal値をそのままQuoteのBigDecimalフィールドに設定 ★★★
        quote.setMonthlyPremium(quoteForm.getMonthlyPremium());
        quote.setBenefit(quoteForm.getInsuranceContent());

        // QuoteFormから各保険タイプ固有のフィールドを直接設定
        // ★★★ 修正箇所: QuoteFormのBigDecimal値をそのままQuoteのBigDecimalフィールドに設定 ★★★
        quote.setDailyHospitalizationFee(quoteForm.getDailyHospitalizationFee());
        quote.setPaymentDays(quoteForm.getPaymentDays()); // Integerのまま
        quote.setBenefitAmount(quoteForm.getBenefitAmount());
        quote.setNumberOfPayments(quoteForm.getNumberOfPayments()); // Integerのまま
        quote.setSurrenderValue(quoteForm.getSurrenderValue());

        quote.setEstimateDate(LocalDate.now());
        quote.setStatus("見積もり済み");

        Long currentUserId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    currentUserId = user.get().getId();
                } else {
                    System.err.println("ERROR: User not found in DB for username: " + username + " during quote creation.");
                }
            }
        }
        
        if (currentUserId != null) {
            quote.setCreatedByUserId(currentUserId);
        } else {
            System.err.println("WARN: Could not get authenticated user ID for quote creation. Setting CreatedByUserId to null.");
            quote.setCreatedByUserId(null);
        }

        // amount フィールドの扱い: monthlyPremium と重複する可能性について考慮が必要
        // もし amount が月払い保険料と同じ意味なら、 monthlyPremium を amount に設定するか、
        // amount フィールド自体を削除して monthlyPremium に統一することを検討してください。
        // ここでは便宜的に monthlyPremium を amount に設定しておきます。
        if (quote.getAmount() == null && quote.getMonthlyPremium() != null) {
             quote.setAmount(quote.getMonthlyPremium().intValue()); // BigDecimalからintへの変換は精度に注意
        }

        return quoteRepository.save(quote);
    }

    @Override
    @Transactional(readOnly = true)
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
