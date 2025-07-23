package jp.co.example.quote_proposal_1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.Estimate;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.User;
import jp.co.example.quote_proposal_1.form.QuoteForm; // QuoteForm を使用するため追加
import jp.co.example.quote_proposal_1.repository.CustomerRepository;
import jp.co.example.quote_proposal_1.repository.EstimateRepository;
import jp.co.example.quote_proposal_1.repository.InsuranceProductRepository;
import jp.co.example.quote_proposal_1.repository.UserRepository;

@Service
@Transactional
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final CustomerRepository customerRepository;
    private final InsuranceProductRepository insuranceProductRepository;
    private final UserRepository userRepository;

    // 保険料データを保持するマップ (Product ID -> 性別 -> 年齢層 -> 月々保険料)
    private Map<Long, Map<String, Map<String, Integer>>> premiumData;

    // 給付金データを保持するマップ (Product ID -> 年齢層 -> 給付金額)
    private Map<Long, Map<String, Integer>> benefitAmountData;
    // 日額入院給付金データを保持するマップ (Product ID -> 年齢層 -> 日額)
    private Map<Long, Map<String, Integer>> dailyHospitalizationFeeData;
    // 支払い回数データを保持するマップ (Product ID -> 年齢層 -> 回数)
    private Map<Long, Map<String, Integer>> numberOfPaymentsData;
    // 支払い日数データを保持するマップ (Product ID -> 年齢層 -> 日数)
    private Map<Long, Map<String, Integer>> paymentDaysData;


    @Autowired
    public EstimateService(EstimateRepository estimateRepository,
                           CustomerRepository customerRepository,
                           InsuranceProductRepository insuranceProductRepository,
                           UserRepository userRepository) {
        this.estimateRepository = estimateRepository;
        this.customerRepository = customerRepository;
        this.insuranceProductRepository = insuranceProductRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct // アプリケーション起動時に一度だけ実行される
    public void initPremiumData() {
        premiumData = new HashMap<>();
        benefitAmountData = new HashMap<>();
        dailyHospitalizationFeeData = new HashMap<>();
        numberOfPaymentsData = new HashMap<>();
        paymentDaysData = new HashMap<>();

        // 終身保険 (product_id = 2)
        Map<String, Map<String, Integer>> lifeInsurance = new HashMap<>();
        Map<String, Integer> lifeMale = new HashMap<>();
        lifeMale.put("0-9", 2000); lifeMale.put("10-19", 2000); lifeMale.put("20-29", 2500);
        lifeMale.put("30-39", 3000); lifeMale.put("40-49", 4000); lifeMale.put("50-59", 6300);
        lifeMale.put("60-69", 9000); lifeMale.put("70-79", 18000);
        lifeInsurance.put("男性", lifeMale);

        Map<String, Integer> lifeFemale = new HashMap<>();
        lifeFemale.put("0-9", 2000); lifeFemale.put("10-19", 1800); lifeFemale.put("20-29", 2000);
        lifeFemale.put("30-39", 2500); lifeFemale.put("40-49", 3500); lifeFemale.put("50-59", 4700);
        lifeFemale.put("60-69", 7000); lifeFemale.put("70-79", 13000);
        lifeInsurance.put("女性", lifeFemale);
        premiumData.put(2L, lifeInsurance);

        Map<String, Integer> lifeBenefit = new HashMap<>();
        lifeBenefit.put("0-79", 2000000); // 200万円
        benefitAmountData.put(2L, lifeBenefit);


        // 医療保険 (product_id = 3)
        Map<String, Map<String, Integer>> medicalInsurance = new HashMap<>();
        Map<String, Integer> medicalMale = new HashMap<>();
        medicalMale.put("0-9", 1000); medicalMale.put("10-19", 1100); medicalMale.put("20-29", 1200);
        medicalMale.put("30-39", 1300); medicalMale.put("40-49", 1400); medicalMale.put("50-59", 1900);
        medicalMale.put("60-69", 3000); medicalMale.put("70-79", 4500);
        medicalInsurance.put("男性", medicalMale);

        Map<String, Integer> medicalFemale = new HashMap<>();
        medicalFemale.put("0-9", 1000); medicalFemale.put("10-19", 1100); medicalFemale.put("20-29", 1200);
        medicalFemale.put("30-39", 1300); medicalFemale.put("40-49", 1400); medicalFemale.put("50-59", 1600);
        medicalFemale.put("60-69", 2500); medicalFemale.put("70-79", 3500);
        medicalInsurance.put("女性", medicalFemale);
        premiumData.put(3L, medicalInsurance);

        Map<String, Integer> medicalDailyFee = new HashMap<>();
        medicalDailyFee.put("0-79", 5000); // 5000円
        dailyHospitalizationFeeData.put(3L, medicalDailyFee);

        Map<String, Integer> medicalPaymentDays = new HashMap<>();
        medicalPaymentDays.put("0-79", 60); // 60日
        paymentDaysData.put(3L, medicalPaymentDays);


        // がん保険 (product_id = 1)
        Map<String, Map<String, Integer>> cancerInsurance = new HashMap<>();
        Map<String, Integer> cancerMale = new HashMap<>();
        cancerMale.put("0-9", 600); cancerMale.put("10-19", 700); cancerMale.put("20-29", 1000);
        cancerMale.put("30-39", 1400); cancerMale.put("40-49", 2000); cancerMale.put("50-59", 2900);
        cancerMale.put("60-69", 4000); cancerMale.put("70-79", 5500);
        cancerInsurance.put("男性", cancerMale);

        Map<String, Integer> cancerFemale = new HashMap<>();
        cancerFemale.put("0-9", 700); cancerFemale.put("10-19", 800); cancerFemale.put("20-29", 1300);
        cancerFemale.put("30-39", 1600); cancerFemale.put("40-49", 1800); cancerFemale.put("50-59", 2300);
        cancerFemale.put("60-69", 3000); cancerFemale.put("70-79", 4000);
        cancerInsurance.put("女性", cancerFemale);
        premiumData.put(1L, cancerInsurance);

        Map<String, Integer> cancerBenefit = new HashMap<>();
        cancerBenefit.put("0-19", 500000); // 50万円
        cancerBenefit.put("20-79", 1000000); // 100万円
        benefitAmountData.put(1L, cancerBenefit);

        Map<String, Integer> cancerPayments = new HashMap<>();
        cancerPayments.put("0-19", 2); // 2回
        cancerPayments.put("20-79", 1); // 1回
        numberOfPaymentsData.put(1L, cancerPayments);
    }

    public QuoteForm getCalculatedPremium(QuoteForm quoteForm) {
        Long productId = quoteForm.getProductId();
        int age = quoteForm.getAge();
        String gender = quoteForm.getGender();

        Integer monthlyPremiumInt = 0; // 一時的にIntegerで保持
        Integer benefitAmountInt = null;
        Integer dailyHospitalizationFeeInt = null;
        Integer numberOfPaymentsInt = null;
        Integer paymentDaysInt = null;
        String insuranceContent = "";

        String ageRange = getAgeRange(age);

        // 月々保険料の取得
        if (premiumData.containsKey(productId)) {
            Map<String, Map<String, Integer>> productData = premiumData.get(productId);
            if (productData.containsKey(gender)) {
                Map<String, Integer> genderData = productData.get(gender);
                if (genderData.containsKey(ageRange)) {
                    monthlyPremiumInt = genderData.get(ageRange);
                } else {
                    System.err.println("WARN: Age range " + ageRange + " not found for gender " + gender + " product " + productId);
                }
            } else {
                System.err.println("WARN: Gender " + gender + " not found for product " + productId);
            }
        } else {
            System.err.println("WARN: Product ID " + productId + " not found in premium data.");
        }

        // 各保険タイプ固有の給付金、日額、回数、日数などの取得
        if (productId == 2L) { // 終身保険
            insuranceContent = "一生涯の死亡保障が得られる終身保険";
            if (benefitAmountData.containsKey(productId) && benefitAmountData.get(productId).containsKey("0-79")) {
                benefitAmountInt = benefitAmountData.get(productId).get("0-79");
            }
        } else if (productId == 3L) { // 医療保険
            insuranceContent = "入院時に日額給付される医療保険";
            if (dailyHospitalizationFeeData.containsKey(productId) && dailyHospitalizationFeeData.get(productId).containsKey("0-79")) {
                dailyHospitalizationFeeInt = dailyHospitalizationFeeData.get(productId).get("0-79");
            }
            if (paymentDaysData.containsKey(productId) && paymentDaysData.get(productId).containsKey("0-79")) {
                paymentDaysInt = paymentDaysData.get(productId).get("0-79");
            }
            benefitAmountInt = dailyHospitalizationFeeInt; // 医療保険では日額を入れる
        } else if (productId == 1L) { // がん保険
            insuranceContent = "がん診断時に一時金給付されるがん保険";
            if (benefitAmountData.containsKey(productId) && benefitAmountData.get(productId).containsKey(ageRange)) {
                benefitAmountInt = benefitAmountData.get(productId).get(ageRange);
            }
            if (numberOfPaymentsData.containsKey(productId) && numberOfPaymentsData.get(productId).containsKey(ageRange)) {
                numberOfPaymentsInt = numberOfPaymentsData.get(productId).get(ageRange);
            }
        }

        // QuoteForm に計算結果を設定
        // Integer から BigDecimal へ変換
        quoteForm.setMonthlyPremium(BigDecimal.valueOf(monthlyPremiumInt));
        quoteForm.setBenefitAmount(benefitAmountInt != null ? BigDecimal.valueOf(benefitAmountInt) : null);
        quoteForm.setDailyHospitalizationFee(dailyHospitalizationFeeInt != null ? BigDecimal.valueOf(dailyHospitalizationFeeInt) : null);
        quoteForm.setNumberOfPayments(numberOfPaymentsInt); // これはIntegerのまま
        quoteForm.setPaymentDays(paymentDaysInt); // これはIntegerのまま
        quoteForm.setInsuranceContent(insuranceContent);
        quoteForm.setSurrenderValue(null); // 解約返戻金はここでは計算しないためnull

        return quoteForm;
    }

    private String getAgeRange(int age) {
        if (age >= 0 && age <= 9) return "0-9";
        if (age >= 10 && age <= 19) return "10-19";
        if (age >= 20 && age <= 29) return "20-29";
        if (age >= 30 && age <= 39) return "30-39";
        if (age >= 40 && age <= 49) return "40-49";
        if (age >= 50 && age <= 59) return "50-59";
        if (age >= 60 && age <= 69) return "60-69";
        if (age >= 70 && age <= 79) return "70-79";
        return "Unknown";
    }

    // --- 既存のメソッドは変更なし ---
    @Transactional(readOnly = true)
    public Optional<Estimate> findEstimateById(Long id) {
        return estimateRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Estimate> findEstimatesByCustomerId(Long customerId) {
        return estimateRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Transactional(readOnly = true)
    public Optional<Estimate> findLatestEstimateByCustomerId(Long customerId) {
        List<Estimate> estimates = estimateRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        if (!estimates.isEmpty()) {
            return Optional.of(estimates.get(0));
        }
        return Optional.empty();
    }

    public Estimate saveEstimate(Long customerId, Long userId, Long productId,
                                 Integer age, String gender, BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("指定された顧客が見つかりません。"));
        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された保険商品が見つかりません。"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("登録ユーザーが見つかりません。"));

        Estimate estimate = new Estimate();
        estimate.setCustomer(customer);
        estimate.setCreatedBy(user);
        estimate.setProduct(product);
        estimate.setAge(age);
        estimate.setGender(gender);
        estimate.setMonthlyPremium(monthlyPremium);
        estimate.setSurrenderValue(surrenderValue);
        estimate.setEstimateDate(LocalDate.now());

        if (estimate.getAmount() == null) {
             estimate.setAmount(monthlyPremium.intValue()); // BigDecimalからintへの変換は精度に注意
        }

        return estimateRepository.save(estimate);
    }

    public Estimate updateEstimate(Long id, Long productId, Integer age, String gender,
                                   BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("更新対象の見積もりが見つかりません。"));

        InsuranceProduct product = insuranceProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された保険商品が見つかりません。"));

        estimate.setProduct(product);
        estimate.setAge(age);
        estimate.setGender(gender);
        estimate.setMonthlyPremium(monthlyPremium);
        estimate.setSurrenderValue(surrenderValue);

        if (estimate.getAmount() == null) {
            estimate.setAmount(monthlyPremium.intValue());
        }

        return estimateRepository.save(estimate);
    }

    @Transactional
    public void deleteEstimateById(Long id) {
        if (!estimateRepository.existsById(id)) {
            throw new IllegalArgumentException("指定されたIDの見積もりが見つかりません: " + id);
        }
        estimateRepository.deleteById(id);
    }
}