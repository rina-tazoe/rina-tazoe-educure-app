package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail;
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.service.CancerInsuranceDetailService;
import jp.co.example.quote_proposal_1.service.InsuranceProductService;
import jp.co.example.quote_proposal_1.service.MedicalInsuranceDetailService;
import jp.co.example.quote_proposal_1.service.QuoteService;
import jp.co.example.quote_proposal_1.service.WholeLifeInsuranceDetailService;
import jp.co.example.quote_proposal_1.validation.ValidationGroups;

@Controller
@RequestMapping("/quote")
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

    private final InsuranceProductService insuranceProductService;
    private final MedicalInsuranceDetailService medicalInsuranceDetailService;
    private final CancerInsuranceDetailService cancerInsuranceDetailService;
    private final WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService;
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(
            InsuranceProductService insuranceProductService,
            MedicalInsuranceDetailService medicalInsuranceDetailService,
            CancerInsuranceDetailService cancerInsuranceDetailService,
            WholeLifeInsuranceDetailService wholeLifeInsuranceDetailService,
            QuoteService quoteService) {
        this.insuranceProductService = insuranceProductService;
        this.medicalInsuranceDetailService = medicalInsuranceDetailService;
        this.cancerInsuranceDetailService = cancerInsuranceDetailService;
        this.wholeLifeInsuranceDetailService = wholeLifeInsuranceDetailService;
        this.quoteService = quoteService;
    }

    @GetMapping
    public String showQuoteForm(Model model) {
        QuoteForm quoteForm = new QuoteForm();
        logger.info("見積もりフォームを生成しました: {}", quoteForm);
        System.out.println("DEBUG: showQuoteForm メソッドが呼び出されました。QuoteForm インスタンス: " + quoteForm);
        model.addAttribute("quoteForm", quoteForm);

        List<InsuranceProduct> products = insuranceProductService.findAll();
        model.addAttribute("products", products);

        return "quote/form";
    }

    @PostMapping("/calculate")
    public String calculateQuote(
            @Validated(ValidationGroups.QuoteCalculation.class) @ModelAttribute QuoteForm quoteForm,
            BindingResult bindingResult,
            Model model) {

        System.out.println("DEBUG: calculateQuote メソッドが呼び出されました。");
        System.out.println("DEBUG: 受信した QuoteForm: " + quoteForm);

        if (bindingResult.hasErrors()) {
            System.out.println("DEBUG: バリデーションエラーがあります: " + bindingResult.getAllErrors());
            List<InsuranceProduct> products = insuranceProductService.findAll();
            model.addAttribute("products", products);
            return "quote/form";
        }

        InsuranceProduct selectedProduct = insuranceProductService.findById(quoteForm.getProductId())
                                        .orElseThrow(() -> {
                                            logger.error("ERROR: IDで保険商品が見つかりません: {}", quoteForm.getProductId());
                                            return new ResponseStatusException(HttpStatus.NOT_FOUND, "選択された保険商品が見つかりませんでした。");
                                        });

        quoteForm.setInsuranceName(selectedProduct.getProductName());

        if ("医療保険".equals(selectedProduct.getProductName())) {
            Optional<MedicalInsuranceDetail> detailOpt = medicalInsuranceDetailService.findByProductIdAndAgeGroup(
                selectedProduct.getProductId(), quoteForm.getAge());

            if (detailOpt.isPresent()) {
                MedicalInsuranceDetail detail = detailOpt.get();

                if ("男性".equals(quoteForm.getGender())) {
                    quoteForm.setMonthlyPremium(detail.getPaymentMonthlyMale());
                } else if ("女性".equals(quoteForm.getGender())) {
                    quoteForm.setMonthlyPremium(detail.getPaymentMonthlyFemale());
                } else {
                    logger.error("ERROR: 医療保険の見積もり計算で性別が不正です。productId: {}, age: {}, gender: {}",
                            selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                    bindingResult.reject("quote.calculation.error", "性別の選択が正しくありません。");
                    model.addAttribute("products", insuranceProductService.findAll());
                    return "quote/form";
                }

                // ★★★ 医療保険固有のフィールドをQuoteFormに設定 ★★★
                quoteForm.setDailyHospitalizationFee(detail.getDailyHospitalizationBenefit());
                quoteForm.setPaymentDays(detail.getPaymentDays());
                quoteForm.setBenefitAmount(null); // 医療保険では使用しない
                quoteForm.setNumberOfPayments(null); // 医療保険では使用しない
                quoteForm.setSurrenderValue(null); // 医療保険では使用しない

                quoteForm.setInsuranceContent("入院日額" + detail.getDailyHospitalizationBenefit() + "円、保障期間" + detail.getPaymentDays() + "日（医療保険）");

            } else {
                logger.error("ERROR: 選択された条件の医療保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                        selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                bindingResult.reject("quote.calculation.error", "選択された年齢と性別の医療保険詳細が見つかりませんでした。");
                model.addAttribute("products", insuranceProductService.findAll());
                return "quote/form";
            }
        } else if ("がん保険".equals(selectedProduct.getProductName())) {
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
                    model.addAttribute("products", insuranceProductService.findAll());
                    return "quote/form";
                }

                // ★★★ がん保険固有のフィールドをQuoteFormに設定 ★★★
                quoteForm.setBenefitAmount(detail.getBenefitAmount() * 10000); // 万円を円に変換して設定
                quoteForm.setNumberOfPayments(detail.getPaymentFrequency());
                quoteForm.setDailyHospitalizationFee(null); // がん保険では使用しない
                quoteForm.setPaymentDays(null); // がん保険では使用しない
                quoteForm.setSurrenderValue(null); // がん保険では使用しない

                quoteForm.setInsuranceContent("一時金" + detail.getBenefitAmount() + "万円、支払い回数" + detail.getPaymentFrequency() + "回（がん保険）");


            } else {
                logger.error("ERROR: 選択された条件のがん保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                        selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                bindingResult.reject("quote.calculation.error", "選択された年齢と性別のがん保険詳細が見つかりませんでした。");
                model.addAttribute("products", insuranceProductService.findAll());
                return "quote/form";
            }
        } else if ("終身保険".equals(selectedProduct.getProductName())) {
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
                    model.addAttribute("products", insuranceProductService.findAll());
                    return "quote/form";
                }

                // ★★★ 終身保険固有のフィールドをQuoteFormに設定 ★★★
                quoteForm.setBenefitAmount(detail.getBenefitAmount() * 10000); // 万円を円に変換して設定
                // 解約返戻金はWholeLifeInsuranceDetailに直接フィールドがない場合、仮の計算で設定
                // 例えば、保険金額の一定割合など (ここでは例として benefitAmountの半分)
                quoteForm.setSurrenderValue(detail.getBenefitAmount() * 5000.0); // 仮の値
                quoteForm.setDailyHospitalizationFee(null); // 終身保険では使用しない
                quoteForm.setPaymentDays(null); // 終身保険では使用しない
                quoteForm.setNumberOfPayments(null); // 終身保険では使用しない

                quoteForm.setInsuranceContent("死亡保障" + detail.getBenefitAmount() + "万円（終身保険）");

            } else {
                logger.error("ERROR: 選択された条件の終身保険詳細が見つかりません。productId: {}, age: {}, gender: {}",
                        selectedProduct.getProductId(), quoteForm.getAge(), quoteForm.getGender());
                bindingResult.reject("quote.calculation.error", "選択された年齢と性別の終身保険詳細が見つかりませんでした。");
                model.addAttribute("products", insuranceProductService.findAll());
                return "quote/form";
            }
        } else {
            logger.error("ERROR: 未知の保険商品タイプ: {}", selectedProduct.getProductName());
            bindingResult.reject("quote.calculation.error", "選択された保険商品の詳細情報が不明です。");
            model.addAttribute("products", insuranceProductService.findAll());
            return "quote/form";
        }

        System.out.println("DEBUG: 計算後のQuoteForm: " + quoteForm);

        model.addAttribute("quoteForm", quoteForm);
        return "quote/result";
    }

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
            model.addAttribute("quoteForm", quoteForm);
            // エラーがある場合は、計算結果画面に戻って再入力
            List<InsuranceProduct> products = insuranceProductService.findAll();
            model.addAttribute("products", products);
            return "quote/result"; // quote/resultにフォワード
        }

        // ★★★ QuoteService.registerQuoteの引数をQuoteFormのみに変更 ★★★
        Quote registeredQuote = quoteService.registerQuote(quoteForm);

        logger.info("見積もり登録処理を実行しました。QuoteForm: {}", quoteForm);

        redirectAttributes.addFlashAttribute("message", "見積もりを登録しました！");
        return "redirect:/quote/detail/" + registeredQuote.getId();
    }

    // 見積もり詳細表示のためのGETマッピング
    @GetMapping("/detail/{id}")
    public String showQuoteDetail(@PathVariable Long id, Model model) {
        Optional<Quote> quoteOpt = quoteService.findById(id);
        if (quoteOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "指定された見積もりが見つかりません。ID: " + id);
        }
        Quote quote = quoteOpt.get();
        // ★★★ HTMLが"estimate"という名前を期待しているため、"estimate"としてモデルに追加 ★★★
        model.addAttribute("estimate", quote);

        return "quote/detail";
    }

    @GetMapping("/completion")
    public String showCompletionPage(Model model) {
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "見積もり登録が完了しました。");
        }
        return "quote/completion";
    }
}