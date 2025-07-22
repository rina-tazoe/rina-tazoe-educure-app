package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional; // Optionalは一部で引き続き使用する可能性があるため残しておく

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.service.CancerInsuranceDetailService;
// ★★★ ここを修正: ProductService から InsuranceProductService へ変更 ★★★
import jp.co.example.quote_proposal_1.service.InsuranceProductService;
import jp.co.example.quote_proposal_1.service.MedicalInsuranceDetailService;
import jp.co.example.quote_proposal_1.service.WholeLifeInsuranceDetailService;
import jp.co.example.quote_proposal_1.validation.ValidationGroups;

@Controller
@RequestMapping("/quote")
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

    // ★★★ ここを修正: ProductService を InsuranceProductService へ変更 ★★★
    private final InsuranceProductService insuranceProductService;
    private final MedicalInsuranceDetailService medicalInsuranceDetailService;
    private final CancerInsuranceDetailService cancerInsuranceDetailService;
    private final WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService;


    @Autowired
    public QuoteController(
            // ★★★ ここを修正: ProductService を InsuranceProductService へ変更 ★★★
            InsuranceProductService insuranceProductService,
            MedicalInsuranceDetailService medicalInsuranceDetailService,
            CancerInsuranceDetailService cancerInsuranceDetailService,
            WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService) {
        // ★★★ ここを修正: this.productService を this.insuranceProductService へ変更 ★★★
        this.insuranceProductService = insuranceProductService;
        this.medicalInsuranceDetailService = medicalInsuranceDetailService;
        this.cancerInsuranceDetailService = cancerInsuranceDetailService;
        this.wholeLifeInsuranceDetailService = wholeLifeInsuranceDetailService;
    }

    /**
     * 見積もり入力フォームを表示します。
     * 保険商品の一覧をモデルに追加します。
     * @param model モデル
     * @return 見積もりフォームのテンプレート名
     */
    @GetMapping
    public String showQuoteForm(Model model) {
        QuoteForm quoteForm = new QuoteForm();
        logger.info("見積もりフォームを生成しました: {}", quoteForm);
        System.out.println("DEBUG: showQuoteForm メソッドが呼び出されました。QuoteForm インスタンス: " + quoteForm);
        model.addAttribute("quoteForm", quoteForm);

        // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
        List<InsuranceProduct> products = insuranceProductService.findAllProducts();
        model.addAttribute("products", products);

        return "quote/form";
    }

    /**
     * 入力された情報に基づいて見積もりを計算し、結果を表示します。
     * @param quoteForm ユーザー入力を含むフォームオブジェクト
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @param redirectAttributes リダイレクト属性（今回は直接使われませんが引数として残します）
     * @return 見積もり結果画面、または入力エラーがあった場合は見積もりフォーム
     */
    @PostMapping("/calculate")
    public String calculateQuote(
            @Validated(ValidationGroups.QuoteCalculation.class) QuoteForm quoteForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("DEBUG: calculateQuote メソッドが呼び出されました。");
        System.out.println("DEBUG: 受信した QuoteForm: " + quoteForm);

        if (bindingResult.hasErrors()) {
            System.out.println("DEBUG: バリデーションエラーがあります: " + bindingResult.getAllErrors());
            // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
            List<InsuranceProduct> products = insuranceProductService.findAllProducts();
            model.addAttribute("products", products);
            return "quote/form";
        }

        // 実際の計算ロジック
        // まず、選択された保険商品エンティティを取得
        // ★★★ ここを修正: Optional でラップせず、直接 InsuranceProduct を受け取る ★★★
        InsuranceProduct selectedProduct = insuranceProductService.findProductById(quoteForm.getProductId());

        // ★★★ ここを修正: isPresent() ではなく null チェックを行う ★★★
        if (selectedProduct != null) {
            // InsuranceProductのフィールドが productId と productName であることを前提とする
            // ★★★ ここを修正: selectedProduct.getProductName() を使用 ★★★
            quoteForm.setInsuranceName(selectedProduct.getProductName());

            // 保険の種類に応じて詳細情報を取得し、計算を行う
            // ★★★ ここを修正: selectedProduct.getProductName() を使用 ★★★
            if ("医療保険".equals(selectedProduct.getProductName())) {
                // Serviceメソッドが findByProductIdAndAgeGroup であることを前提とする
                // ★★★ ここを修正: selectedProduct.getProductId() を使用 ★★★
                Optional<MedicalInsuranceDetail> detailOpt = medicalInsuranceDetailService.findByProductIdAndAgeGroup(
                    selectedProduct.getProductId(), quoteForm.getAge());

                if (detailOpt.isPresent()) {
                    MedicalInsuranceDetail detail = detailOpt.get();

                    // 性別に応じて月額保険料を設定
                    // ★★★ ここを修正: getPaymentMonthlyMale() と getPaymentMonthlyFemale() を使用 ★★★
                    if ("男性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyMale());
                    } else if ("女性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyFemale());
                    } else {
                        logger.error("ERROR: 医療保険の見積もり計算で性別が不正です。productId: {}, age: {}, gender: {}",
                                selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                        bindingResult.reject("quote.calculation.error", "性別の選択が正しくありません。");
                        // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                        model.addAttribute("products", insuranceProductService.findAllProducts());
                        return "quote/form";
                    }

                    // その他の医療保険詳細情報を設定
                    if (detail.getDailyHospitalizationBenefit() != null) {
                        quoteForm.setDailyHospitalizationBenefit(detail.getDailyHospitalizationBenefit() + "円");
                    }
                    if (detail.getPaymentDays() != null) {
                        quoteForm.setPaymentDays(detail.getPaymentDays() + "日");
                    }
                    quoteForm.setInsuranceContent("入院日額" + quoteForm.getDailyHospitalizationBenefit() + "、保障期間" + quoteForm.getPaymentDays() + "（医療保険）");

                } else {
                    logger.error("ERROR: 選択された条件の医療保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                            selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                    bindingResult.reject("quote.calculation.error", "選択された年齢と性別の医療保険詳細が見つかりませんでした。");
                    // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                    model.addAttribute("products", insuranceProductService.findAllProducts());
                    return "quote/form";
                }
            // ★★★ ここを修正: selectedProduct.getProductName() を使用 ★★★
            } else if ("がん保険".equals(selectedProduct.getProductName())) {
                // Serviceメソッドが findByProductIdAndAgeGroup であることを前提とする
                // ★★★ ここを修正: selectedProduct.getProductId() を使用 ★★★
                Optional<CancerInsuranceDetail> detailOpt = cancerInsuranceDetailService.findByProductIdAndAgeGroup(
                    selectedProduct.getProductId(), quoteForm.getAge());

                if (detailOpt.isPresent()) {
                    CancerInsuranceDetail detail = detailOpt.get();

                    if ("男性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyMale());
                    } else if ("女性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyFemale());
                    } else {
                        logger.error("ERROR: がん保険の見積もり計算で性別が不正です。productId: {}, age: {}, gender: {}",
                                selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                        bindingResult.reject("quote.calculation.error", "性別の選択が正しくありません。");
                        // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                        model.addAttribute("products", insuranceProductService.findAllProducts());
                        return "quote/form";
                    }

                    quoteForm.setBenefitAmount(detail.getBenefitAmount() + "万円");
                    quoteForm.setPaymentFrequency(detail.getPaymentFrequency() + "回");
                    quoteForm.setInsuranceContent("一時金" + quoteForm.getBenefitAmount() + "（がん保険）");

                } else {
                    logger.error("ERROR: 選択された条件のがん保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                            selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                    bindingResult.reject("quote.calculation.error", "選択された年齢と性別のがん保険詳細が見つかりませんでした。");
                    // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                    model.addAttribute("products", insuranceProductService.findAllProducts());
                    return "quote/form";
                }
            // ★★★ ここを修正: selectedProduct.getProductName() を使用 ★★★
            } else if ("終身保険".equals(selectedProduct.getProductName())) {
                // Serviceメソッドが findByProductIdAndAgeGroup であることを前提とする
                // ★★★ ここを修正: selectedProduct.getProductId() を使用 ★★★
                Optional<WholeLifeInsuranceDetail> detailOpt = wholeLifeInsuranceDetailService.findByProductIdAndAgeGroup(
                    selectedProduct.getProductId(), quoteForm.getAge());

                if (detailOpt.isPresent()) {
                    WholeLifeInsuranceDetail detail = detailOpt.get();

                    if ("男性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyMale());
                    } else if ("女性".equals(quoteForm.getGender())) {
                        quoteForm.setMonthlyPremium(detail.getPaymentMonthlyFemale());
                    } else {
                        logger.error("ERROR: 終身保険の見積もり計算で性別が不正です。productId: {}, age: {}, gender: {}",
                                selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                        bindingResult.reject("quote.calculation.error", "性別の選択が正しくありません。");
                        // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                        model.addAttribute("products", insuranceProductService.findAllProducts());
                        return "quote/form";
                    }

                    quoteForm.setBenefitAmount(detail.getBenefitAmount() + "万円");
                    quoteForm.setInsuranceContent("死亡保障" + quoteForm.getBenefitAmount() + "（終身保険）");

                } else {
                    logger.error("ERROR: 選択された条件の終身保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                            selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                    bindingResult.reject("quote.calculation.error", "選択された年齢と性別の終身保険詳細が見つかりませんでした。");
                    // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                    model.addAttribute("products", insuranceProductService.findAllProducts());
                    return "quote/form";
                }
            } else {
                // ★★★ ここを修正: selectedProduct.getProductName() を使用 ★★★
                logger.error("ERROR: 未知の保険商品タイプ: {}", selectedProduct.getProductName());
                bindingResult.reject("quote.calculation.error", "選択された保険商品の詳細情報が不明です。");
                // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
                model.addAttribute("products", insuranceProductService.findAllProducts());
                return "quote/form";
            }

        } else {
            logger.error("ERROR: IDで保険商品が見つかりません: {}", quoteForm.getProductId());
            bindingResult.reject("quote.calculation.error", "選択された保険商品が見つかりませんでした。");
            // ★★★ ここを修正: productService を insuranceProductService へ変更 ★★★
            model.addAttribute("products", insuranceProductService.findAllProducts());
            return "quote/form";
        }

        // 計算後のQuoteFormをModelに追加し、結果画面へ遷移
        model.addAttribute("quoteForm", quoteForm);
        return "quote/result"; // templates/quote/result.html を表示
    }

    /**
     * 見積もり結果画面で入力された顧客情報を登録します。
     * @param quoteForm 登録情報を含むフォームオブジェクト
     * @param bindingResult バリデーション結果
     * @param model モデル
     * @param redirectAttributes リダイレクト属性
     * @return 登録完了後の詳細画面へリダイレクト
     */
    @PostMapping("/register")
    public String registerQuote(
            @Validated(ValidationGroups.CustomerRegistration.class) @ModelAttribute QuoteForm quoteForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("DEBUG: registerQuote メソッドが呼び出されました。");
        System.out.println("DEBUG: 登録のために受信した QuoteForm: " + quoteForm);

        if (bindingResult.hasErrors()) {
            System.out.println("DEBUG: 登録バリデーションエラーがあります: " + bindingResult.getAllErrors());
            // エラーがある場合は、見積もり結果画面に戻り、エラーメッセージを表示
            // 見積もり内容は再計算しないので、quoteFormはそのままModelに渡す
            return "quote/result";
        }

        // TODO: ここで顧客情報と見積もり結果をデータベースに登録するロジックを実装
        // 例: estimateService.saveEstimate(quoteForm);
        logger.info("見積もり登録処理を実行しました。QuoteForm: {}", quoteForm);

        Long quoteId = 123L; // 仮のID。実際にはデータベース登録後に生成されるIDを使用
        redirectAttributes.addFlashAttribute("message", "見積もりを登録しました！");
        return "redirect:/quote/detail/" + quoteId;
    }
}