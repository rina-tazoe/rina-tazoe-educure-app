package jp.co.example.quote_proposal_1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.PostConstruct; // @PostConstruct のインポート

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
    private UserRepository userRepository;

    // 保険料データマップ
    private Map<Long, Map<String, Integer>> premiumData;
    private Map<Long, Map<String, Integer>> benefitAmountData;
    private Map<Long, Map<String, Integer>> dailyHospitalizationFeeData;
    private Map<Long, Map<String, Integer>> paymentDaysData;
    private Map<Long, Integer> numberOfPaymentsData; 
    private Map<Long, Map<String, Integer>> surrenderValueData; 

    // アプリケーション起動時にデータ初期化
    @PostConstruct
    public void initPremiumData() {
        premiumData = new HashMap<>();
        benefitAmountData = new HashMap<>();
        dailyHospitalizationFeeData = new HashMap<>();
        paymentDaysData = new HashMap<>();
        numberOfPaymentsData = new HashMap<>();
        surrenderValueData = new HashMap<>();

        // 商品ID 1L: がん保険 (ご提示いただいたデータに修正)
        Map<String, Integer> cancerPremium = new HashMap<>();
        cancerPremium.put("0-9男性", 600); cancerPremium.put("0-9女性", 700);
        cancerPremium.put("10-19男性", 700); cancerPremium.put("10-19女性", 800);
        cancerPremium.put("20-29男性", 1000); cancerPremium.put("20-29女性", 1300);
        cancerPremium.put("30-39男性", 1400); cancerPremium.put("30-39女性", 1600);
        cancerPremium.put("40-49男性", 2000); cancerPremium.put("40-49女性", 1800);
        cancerPremium.put("50-59男性", 2900); cancerPremium.put("50-59女性", 2300);
        cancerPremium.put("60-69男性", 4000); cancerPremium.put("60-69女性", 3000);
        cancerPremium.put("70-79男性", 5500); cancerPremium.put("70-79女性", 4000);
        premiumData.put(1L, cancerPremium);

        Map<String, Integer> cancerBenefit = new HashMap<>();
        cancerBenefit.put("0-19", 500000); // 50万
        cancerBenefit.put("20-79", 1000000); // 100万
        benefitAmountData.put(1L, cancerBenefit);

        // がん保険の支払い回数（年齢層で分岐）
        Map<String, Integer> cancerNumberOfPayments = new HashMap<>();
        cancerNumberOfPayments.put("0-19", 2); // 2回
        cancerNumberOfPayments.put("20-79", 1); // 1回
        // numberOfPaymentsData.put(1L, ???) がMap<Long, Integer>なので直接設定できない。
        // getCalculatedPremium内でageRangeと合わせて取得するように変更が必要。
        // 現状はgetCalculatedPremium内で直接設定するロジックの方がシンプルかもしれません。


        // 商品ID 2L: 終身保険 (ご提示いただいたデータに修正)
        Map<String, Integer> lifePremium = new HashMap<>();
        lifePremium.put("0-9男性", 2000); lifePremium.put("0-9女性", 2000);
        lifePremium.put("10-19男性", 2000); lifePremium.put("10-19女性", 1800);
        lifePremium.put("20-29男性", 2500); lifePremium.put("20-29女性", 2000);
        lifePremium.put("30-39男性", 3000); lifePremium.put("30-39女性", 2500);
        lifePremium.put("40-49男性", 4000); lifePremium.put("40-49女性", 3500);
        lifePremium.put("50-59男性", 6300); lifePremium.put("50-59女性", 4700);
        lifePremium.put("60-69男性", 9000); lifePremium.put("60-69女性", 7000);
        lifePremium.put("70-79男性", 18000); lifePremium.put("70-79女性", 13000);
        premiumData.put(2L, lifePremium);

        Map<String, Integer> lifeBenefit = new HashMap<>();
        lifeBenefit.put("0-79", 2000000); // 200万
        benefitAmountData.put(2L, lifeBenefit);
        // 終身保険の支払回数は固定値のまま (例: 60回払い)
        numberOfPaymentsData.put(2L, 60); 

        // 商品ID 3L: 医療保険 (ご提示いただいたデータに修正)
        Map<String, Integer> medicalPremium = new HashMap<>();
        medicalPremium.put("0-9男性", 1000); medicalPremium.put("0-9女性", 1000);
        medicalPremium.put("10-19男性", 1100); medicalPremium.put("10-19女性", 1100);
        medicalPremium.put("20-29男性", 1200); medicalPremium.put("20-29女性", 1200);
        medicalPremium.put("30-39男性", 1300); medicalPremium.put("30-39女性", 1300);
        medicalPremium.put("40-49男性", 1400); medicalPremium.put("40-49女性", 1400);
        medicalPremium.put("50-59男性", 1900); medicalPremium.put("50-59女性", 1600);
        medicalPremium.put("60-69男性", 3000); medicalPremium.put("60-69女性", 2500);
        medicalPremium.put("70-79男性", 4500); medicalPremium.put("70-79女性", 3500);
        premiumData.put(3L, medicalPremium);

        Map<String, Integer> medicalDailyHospitalizationFee = new HashMap<>();
        medicalDailyHospitalizationFee.put("0-79", 5000); // すべての年齢層で日額5000円と仮定
        dailyHospitalizationFeeData.put(3L, medicalDailyHospitalizationFee);

        Map<String, Integer> medicalPaymentDays = new HashMap<>();
        medicalPaymentDays.put("0-79", 60); // すべての年齢層で最大支払い日数60日と仮定
        paymentDaysData.put(3L, medicalPaymentDays);
        
        // 解約返戻金データの仮設定 (今回は簡略化のためデフォルト0)
        Map<String, Integer> commonSurrenderValue = new HashMap<>();
        commonSurrenderValue.put("default", 0);
        surrenderValueData.put(1L, commonSurrenderValue); // がん保険
        surrenderValueData.put(2L, commonSurrenderValue); // 終身保険
        surrenderValueData.put(3L, commonSurrenderValue); // 医療保険
    }

    // 見積もり計算ロジック
    @Override
    public QuoteForm getCalculatedPremium(QuoteForm quoteForm) {
        System.out.println("DEBUG: QuoteService.getCalculatedPremium メソッドが呼び出されました。");
        System.out.println("DEBUG: QuoteForm in QuoteService (before calculation): " + quoteForm);

        Long productId = quoteForm.getProductId();
        int age = quoteForm.getAge();
        String gender = quoteForm.getGender();

        String ageRange = getAgeRange(age); // 修正された getAgeRange を使用
        System.out.println("DEBUG: Age Range: " + ageRange);

        // 保険商品情報を取得
        InsuranceProduct product = insuranceProductService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        quoteForm.setInsuranceName(product.getProductName());
        String insuranceContent = product.getProductDescription(); // 初期値は商品DBから取得した説明文


        // 月々保険料の計算
        Integer monthlyPremiumInt = null;
        if (premiumData.containsKey(productId)) {
            Map<String, Integer> productPremium = premiumData.get(productId);
            String key = ageRange + gender; 
            if (productPremium.containsKey(key)) {
                monthlyPremiumInt = productPremium.get(key);
            } else {
                System.err.println("WARN: Premium data not found for age range " + ageRange + " and gender " + gender + " for product " + productId);
            }
        } else {
            System.err.println("WARN: Product ID " + productId + " not found in premium data.");
        }
        quoteForm.setMonthlyPremium(monthlyPremiumInt != null ? BigDecimal.valueOf(monthlyPremiumInt) : null);
        System.out.println("DEBUG: Calculated Monthly Premium: " + monthlyPremiumInt);


        // 保険種類ごとの固有情報の計算と設定
        Integer benefitAmountInt = null;
        Integer dailyHospitalizationFeeInt = null;
        Integer paymentDaysInt = null;
        Integer numberOfPaymentsInt = null;
        Integer surrenderValueInt = null;
        

        if (productId == 1L) { // がん保険
            // 給付金
            if (benefitAmountData.containsKey(productId)) {
                // がん保険の給付金は年齢範囲で変わるため、getAgeRangeの戻り値で取得
                if (age >= 0 && age <= 19) {
                    benefitAmountInt = benefitAmountData.get(productId).get("0-19");
                } else if (age >= 20 && age <= 79) {
                    benefitAmountInt = benefitAmountData.get(productId).get("20-79");
                }
                if (benefitAmountInt != null) {
                    insuranceContent += " - 診断給付金 " + String.format("%,d", benefitAmountInt) + "円";
                } else {
                    insuranceContent += " - 診断給付金 - 円";
                }
            } else {
                insuranceContent += " - 診断給付金 - 円";
            }
            
            // 支払い回数
            if (age >= 0 && age <= 19) { // 0-19歳は2回
                numberOfPaymentsInt = 2;
                insuranceContent += "、支払い回数 " + numberOfPaymentsInt + "回";
            } else if (age >= 20 && age <= 79) { // 20-79歳は1回
                numberOfPaymentsInt = 1;
                insuranceContent += "、支払い回数 " + numberOfPaymentsInt + "回";
            } else {
                insuranceContent += "、支払い回数 - 回";
            }

            if (surrenderValueData.containsKey(productId)) {
                surrenderValueInt = surrenderValueData.get(productId).get("default");
            }
            dailyHospitalizationFeeInt = null; // がん保険では日額入院費は通常なし
            paymentDaysInt = null; // がん保険では支払い期間は通常なし

        } else if (productId == 2L) { // 終身保険
            // 給付金
            if (benefitAmountData.containsKey(productId) && benefitAmountData.get(productId).containsKey("0-79")) { 
                benefitAmountInt = benefitAmountData.get(productId).get("0-79");
                insuranceContent += " - 死亡保障 " + String.format("%,d", benefitAmountInt) + "円";
            } else {
                insuranceContent += " - 死亡保障 - 円";
            }

            // 支払回数
            if (numberOfPaymentsData.containsKey(productId)) {
                numberOfPaymentsInt = numberOfPaymentsData.get(productId);
                insuranceContent += " / 支払回数 " + numberOfPaymentsInt + "回";
            } else {
                insuranceContent += " / 支払回数 - 回";
            }

            if (surrenderValueData.containsKey(productId)) {
                surrenderValueInt = surrenderValueData.get(productId).get("default");
            }
            dailyHospitalizationFeeInt = null;
            paymentDaysInt = null;

        } else if (productId == 3L) { // 医療保険
            // 日額入院費
            if (dailyHospitalizationFeeData.containsKey(productId) && dailyHospitalizationFeeData.get(productId).containsKey("0-79")) { 
                dailyHospitalizationFeeInt = dailyHospitalizationFeeData.get(productId).get("0-79"); // 固定値取得
                insuranceContent += " - 1日あたり入院費 " + String.format("%,d", dailyHospitalizationFeeInt) + "円";
            } else {
                insuranceContent += " - 1日あたり入院費 - 円";
                System.err.println("WARN: Daily hospitalization fee data not found for age range " + ageRange + " for product " + productId);
            }
            
            // 最大支払い日数
            if (paymentDaysData.containsKey(productId) && paymentDaysData.get(productId).containsKey("0-79")) { 
                paymentDaysInt = paymentDaysData.get(productId).get("0-79"); // 固定値取得
                insuranceContent += "、最大支払い日数 " + paymentDaysInt + "日";
            } else {
                insuranceContent += "、最大支払い日数 - 日";
                System.err.println("WARN: Payment days data not found for age range " + ageRange + " for product " + productId);
            }
            
            // 医療保険の場合、benefitAmount は dailyHospitalizationFee に対応する
            benefitAmountInt = dailyHospitalizationFeeInt; 
            if (surrenderValueData.containsKey(productId)) {
                surrenderValueInt = surrenderValueData.get(productId).get("default");
            }
            numberOfPaymentsInt = null;
        } else {
            // 未知の保険商品IDの場合のデフォルト処理
            quoteForm.setMonthlyPremium(BigDecimal.ZERO);
            quoteForm.setBenefitAmount(null);
            quoteForm.setDailyHospitalizationFee(null);
            quoteForm.setNumberOfPayments(null);
            quoteForm.setPaymentDays(null);
            quoteForm.setSurrenderValue(null);
            insuranceContent = "選択された保険商品の詳細はありません。"; 
        }

        quoteForm.setBenefitAmount(benefitAmountInt != null ? BigDecimal.valueOf(benefitAmountInt) : null);
        quoteForm.setDailyHospitalizationFee(dailyHospitalizationFeeInt != null ? BigDecimal.valueOf(dailyHospitalizationFeeInt) : null);
        quoteForm.setPaymentDays(paymentDaysInt);
        quoteForm.setNumberOfPayments(numberOfPaymentsInt);
        quoteForm.setSurrenderValue(surrenderValueInt != null ? BigDecimal.valueOf(surrenderValueInt) : null);
        quoteForm.setInsuranceContent(insuranceContent); 
        
        System.out.println("DEBUG: Final QuoteForm after detailed calculation: " + quoteForm);

        return quoteForm;
    }

    // 年齢層を取得するヘルパーメソッド (ご提示いただいた年齢層に合わせて修正)
    private String getAgeRange(int age) {
        if (age >= 0 && age <= 9) return "0-9";
        if (age >= 10 && age <= 19) return "10-19";
        if (age >= 20 && age <= 29) return "20-29";
        if (age >= 30 && age <= 39) return "30-39";
        if (age >= 40 && age <= 49) return "40-49";
        if (age >= 50 && age <= 59) return "50-59";
        if (age >= 60 && age <= 69) return "60-69"; // 修正
        if (age >= 70 && age <= 79) return "70-79"; // 修正
        return "その他"; 
    }

    // registerQuote メソッド
    @Override
    public Quote registerQuote(QuoteForm quoteForm) {
        System.out.println("DEBUG: registerQuote method called for QuoteForm: " + quoteForm);

        Customer customer;
        Optional<Customer> existingCustomer = customerService.findByEmail(quoteForm.getEmail());

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            customer.setLastName(quoteForm.getLastName());
            customer.setFirstName(quoteForm.getFirstName());
            customer.setDateOfBirth(quoteForm.getDateOfBirth());
            customer.setPhoneNumber(quoteForm.getPhoneNumber());
            customer = customerService.saveCustomer(customer);
            System.out.println("DEBUG: 既存の顧客情報を更新しました。ID: " + customer.getId() + ", Email: " + customer.getEmail());
        } else {
            customer = new Customer();
            customer.setLastName(quoteForm.getLastName());
            customer.setFirstName(quoteForm.getFirstName());
            customer.setDateOfBirth(quoteForm.getDateOfBirth());
            customer.setPhoneNumber(quoteForm.getPhoneNumber());
            customer.setEmail(quoteForm.getEmail());
            customer = customerService.saveCustomer(customer);
            System.out.println("DEBUG: 新規顧客を登録しました。ID: " + customer.getId() + ", Email: " + customer.getEmail());
        }

        InsuranceProduct selectedProduct = insuranceProductService.findById(quoteForm.getProductId())
                .orElseThrow(() -> new RuntimeException("選択された保険商品が見つかりませんでした。ID: " + quoteForm.getProductId()));

        Quote quote = new Quote();
        quote.setInsuranceProduct(selectedProduct);
        quote.setCustomer(customer);
        quote.setAge(quoteForm.getAge());
        quote.setGender(quoteForm.getGender());

        quote.setMonthlyPremium(quoteForm.getMonthlyPremium());
        quote.setBenefit(quoteForm.getInsuranceContent()); 

        quote.setDailyHospitalizationFee(quoteForm.getDailyHospitalizationFee());
        quote.setPaymentDays(quoteForm.getPaymentDays());
        quote.setBenefitAmount(quoteForm.getBenefitAmount());
        quote.setNumberOfPayments(quoteForm.getNumberOfPayments());
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

        return quoteRepository.save(quote);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findById(Long id) {
        return quoteRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Quote> findQuotesByCustomerId(Long customerId) {
        return quoteRepository.findByCustomerIdOrderByEstimateDateDesc(customerId);
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
    
    @Override
    public void deleteQuoteById(Long id) {
        quoteRepository.deleteById(id);
    }
}