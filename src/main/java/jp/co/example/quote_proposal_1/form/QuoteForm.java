package jp.co.example.quote_proposal_1.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jp.co.example.quote_proposal_1.validation.ValidationGroups;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteForm {

    // 見積もり計算用
    @NotNull(groups = ValidationGroups.QuoteCalculation.class)
    private Long productId;

    @NotNull(groups = ValidationGroups.QuoteCalculation.class)
    @Min(value = 0, groups = ValidationGroups.QuoteCalculation.class)
    private Integer age;

    @NotBlank(groups = ValidationGroups.QuoteCalculation.class)
    private String gender;

    // 計算結果表示用（Controllerで設定）
    private String insuranceName;
    private String insuranceContent;
    private Integer monthlyPremium;

    // 保険種別によって設定される詳細情報
    private Integer dailyHospitalizationFee; // 医療保険用
    private Integer paymentDays;             // 医療保険用
    private Integer benefitAmount;           // がん保険、終身保険用
    private Integer numberOfPayments;        // ★この行が追加されていることを確認★
    private Double surrenderValue;           // 終身保険用

    // 顧客情報登録用
    @NotBlank(groups = ValidationGroups.CustomerRegistration.class)
    @Length(max = 50, groups = ValidationGroups.CustomerRegistration.class)
    private String lastName;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class)
    @Length(max = 50, groups = ValidationGroups.CustomerRegistration.class)
    private String firstName;

    @NotNull(groups = ValidationGroups.CustomerRegistration.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class)
    @Pattern(regexp = "^\\d{10,11}$", groups = ValidationGroups.CustomerRegistration.class)
    private String phoneNumber;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class)
    @Email(groups = ValidationGroups.CustomerRegistration.class)
    @Length(max = 255, groups = ValidationGroups.CustomerRegistration.class)
    private String email;

    // コンストラクタ (必要であれば維持)
    public QuoteForm(Long productId, Integer age, String gender) {
        this.productId = productId;
        this.age = age;
        this.gender = gender;
    }

    // toStringメソッドをオーバーライドして、デバッグログを分かりやすくする
    @Override
    public String toString() {
        return "QuoteForm{" +
               "productId=" + productId +
               ", age=" + age +
               ", gender='" + gender + '\'' +
               ", insuranceName='" + insuranceName + '\'' +
               ", insuranceContent='" + insuranceContent + '\'' +
               ", monthlyPremium=" + monthlyPremium +
               ", dailyHospitalizationFee=" + dailyHospitalizationFee +
               ", paymentDays=" + paymentDays +
               ", benefitAmount=" + benefitAmount +
               ", numberOfPayments=" + numberOfPayments + // ★ここも確認★
               ", surrenderValue=" + surrenderValue +
               ", lastName='" + (lastName != null ? lastName : "null") + '\'' +
               ", firstName='" + (firstName != null ? firstName : "null") + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "null") + '\'' +
               ", email='" + (email != null ? email : "null") + '\'' +
               '}';
    }
}