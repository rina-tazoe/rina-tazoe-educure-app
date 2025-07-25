package jp.co.example.quote_proposal_1.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import jp.co.example.quote_proposal_1.validation.ValidationGroups;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteForm {

    @NotNull(groups = ValidationGroups.QuoteCalculation.class, message = "保険商品を選択してください。")
    private Long productId;

    @NotNull(groups = ValidationGroups.QuoteCalculation.class, message = "年齢を入力してください。")
    @Min(value = 0, groups = ValidationGroups.QuoteCalculation.class, message = "年齢は0歳以上である必要があります。")
    private Integer age;

    @NotBlank(groups = ValidationGroups.QuoteCalculation.class, message = "性別を選択してください。")
    private String gender;

    // 計算結果表示
    private String insuranceName;
    private String insuranceContent;
    
    private BigDecimal monthlyPremium; 
    private BigDecimal dailyHospitalizationFee; 
    private Integer paymentDays;
    private BigDecimal benefitAmount; 
    private Integer numberOfPayments;
    private BigDecimal surrenderValue;

    // 顧客情報登録
    @NotBlank(groups = ValidationGroups.CustomerRegistration.class, message = "姓は必須です。")
    @Size(max = 50, groups = ValidationGroups.CustomerRegistration.class, message = "姓は50文字以内で入力してください。")
    private String lastName;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class, message = "名は必須です。")
    @Size(max = 50, groups = ValidationGroups.CustomerRegistration.class, message = "名は50文字以内で入力してください。")
    private String firstName;

    @NotNull(groups = ValidationGroups.CustomerRegistration.class, message = "生年月日は必須です。")
    @PastOrPresent(groups = ValidationGroups.CustomerRegistration.class, message = "生年月日は未来の日付であってはなりません。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class, message = "電話番号は必須です。")
    @Pattern(regexp = "^[0-9]{10,11}$", groups = ValidationGroups.CustomerRegistration.class, message = "電話番号は10桁または11桁の数字で入力してください。")
    private String phoneNumber;

    @NotBlank(groups = ValidationGroups.CustomerRegistration.class, message = "メールアドレスは必須です。")
    @Email(groups = ValidationGroups.CustomerRegistration.class, message = "有効なメールアドレスを入力してください。")
    @Size(max = 255, groups = ValidationGroups.CustomerRegistration.class, message = "メールアドレスは255文字以内で入力してください。")
    private String email;

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
                ", numberOfPayments=" + numberOfPayments +
                ", surrenderValue=" + surrenderValue +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}