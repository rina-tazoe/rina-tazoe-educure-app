package jp.co.example.quote_proposal_1.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// import lombok.Data; // この行はコメントアウトのまま
import org.springframework.format.annotation.DateTimeFormat;

import jp.co.example.quote_proposal_1.validation.ValidationGroups; // ★★★ 追加 ★★★

// @Data // この行はコメントアウトのまま
public class QuoteForm {

    // 見積もり内容 (QuoteCalculation グループでバリデート)
    @NotNull(message = "年齢を選択してください", groups = ValidationGroups.QuoteCalculation.class)
    @Min(value = 0, message = "年齢は0歳以上である必要があります", groups = ValidationGroups.QuoteCalculation.class)
    private Integer age;

    @NotBlank(message = "性別を選択してください", groups = ValidationGroups.QuoteCalculation.class)
    private String gender; // "男性" or "女性"

    @NotNull(message = "保険商品を選択してください", groups = ValidationGroups.QuoteCalculation.class)
    private Long productId; // InsuranceProductのID

    // 顧客情報 (CustomerRegistration グループでバリデート)
    @NotBlank(message = "姓は必須です", groups = ValidationGroups.CustomerRegistration.class)
    private String lastName;

    @NotBlank(message = "名は必須です", groups = ValidationGroups.CustomerRegistration.class)
    private String firstName;

    @NotNull(message = "生年月日は必須です", groups = ValidationGroups.CustomerRegistration.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "電話番号は必須です", groups = ValidationGroups.CustomerRegistration.class)
    @Pattern(regexp = "^\\d{10,11}$", message = "有効な電話番号を入力してください (ハイフンなし10桁または11桁)", groups = ValidationGroups.CustomerRegistration.class)
    private String phoneNumber;

    @NotBlank(message = "メールアドレスは必須です", groups = ValidationGroups.CustomerRegistration.class)
    @Email(message = "有効なメールアドレスを入力してください", groups = ValidationGroups.CustomerRegistration.class)
    private String email;

    // 見積もり結果 (計算後に設定されるため、初期値は不要)
    private String insuranceName;
    private String insuranceContent;
    private Integer monthlyPremium;
    private String benefitAmount; // 文字列型で保持 (例: "50万", "200万")
    private String paymentFrequency; // がん保険の支払い回数 (例: "1回", "2回")
    private String dailyHospitalizationBenefit; // 医療保険の1日につき入院費 (例: "5000円")
    private String paymentDays; // 医療保険の支払い日数 (例: "60日")

    // フォームの初期表示用コンストラクタ（見積もり画面用）
    public QuoteForm() {
    }

    // 全てのフィールドに対するgetterとsetterをここに追加
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getInsuranceName() { return insuranceName; }
    public void setInsuranceName(String insuranceName) { this.insuranceName = insuranceName; }
    public String getInsuranceContent() { return insuranceContent; }
    public void setInsuranceContent(String insuranceContent) { this.insuranceContent = insuranceContent; }
    public Integer getMonthlyPremium() { return monthlyPremium; }
    public void setMonthlyPremium(Integer monthlyPremium) { this.monthlyPremium = monthlyPremium; }
    public String getBenefitAmount() { return benefitAmount; }
    public void setBenefitAmount(String benefitAmount) { this.benefitAmount = benefitAmount; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getDailyHospitalizationBenefit() { return dailyHospitalizationBenefit; }
    public void setDailyHospitalizationBenefit(String dailyHospitalizationBenefit) { this.dailyHospitalizationBenefit = dailyHospitalizationBenefit; }
    public String getPaymentDays() { return paymentDays; }
    public void setPaymentDays(String paymentDays) { this.paymentDays = paymentDays; }
}
