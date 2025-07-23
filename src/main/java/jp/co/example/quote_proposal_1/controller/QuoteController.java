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

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.service.InsuranceProductService;
import jp.co.example.quote_proposal_1.service.QuoteService;
import jp.co.example.quote_proposal_1.validation.ValidationGroups;

@Controller
@RequestMapping("/quote")
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

    private final InsuranceProductService insuranceProductService;
    private final QuoteService quoteService; 

    @Autowired
    public QuoteController(
            InsuranceProductService insuranceProductService,
            QuoteService quoteService) { 
        this.insuranceProductService = insuranceProductService;
        this.quoteService = quoteService;
    }

    /**
     * 見積もり入力フォームを表示する
     */
    @GetMapping("/form")
    public String showQuoteForm(Model model) {
        QuoteForm quoteForm = new QuoteForm();
        logger.info("見積もりフォームを生成しました: {}", quoteForm);
        System.out.println("DEBUG: showQuoteForm メソッドが呼び出されました。QuoteForm インスタンス: " + quoteForm);
        model.addAttribute("quoteForm", quoteForm);

        List<InsuranceProduct> products = insuranceProductService.findAll();
        model.addAttribute("products", products);

        return "quote/form";
    }

    /**
     * 見積もりを計算し、結果を表示する
     */
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

        // QuoteService の getCalculatedPremium メソッドを呼び出す
        QuoteForm calculatedQuoteForm = quoteService.getCalculatedPremium(quoteForm);

        // 保険商品名を設定
        InsuranceProduct selectedProduct = insuranceProductService.findById(calculatedQuoteForm.getProductId())
                .orElseThrow(() -> {
                    logger.error("ERROR: IDで保険商品が見つかりません: {}", calculatedQuoteForm.getProductId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "選択された保険商品が見つかりませんでした。");
                });
        calculatedQuoteForm.setInsuranceName(selectedProduct.getProductName());

        System.out.println("DEBUG: 計算後のQuoteForm: " + calculatedQuoteForm);

        model.addAttribute("quoteForm", calculatedQuoteForm);
        return "quote/result"; // 計算結果画面へ遷移
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
            List<InsuranceProduct> products = insuranceProductService.findAll();
            model.addAttribute("products", products);
            return "quote/result";
        }

        Quote registeredQuote = quoteService.registerQuote(quoteForm);

        logger.info("見積もり登録処理を実行しました。QuoteForm: {}", quoteForm);

        redirectAttributes.addFlashAttribute("message", "見積もりを登録しました！");
        return "redirect:/quote/detail/" + registeredQuote.getId();
    }

    @GetMapping("/detail/{id}")
    public String showQuoteDetail(@PathVariable Long id, Model model) {
        Optional<Quote> quoteOpt = quoteService.findById(id);
        if (quoteOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "指定された見積もりが見つかりません。ID: " + id);
        }
        Quote quote = quoteOpt.get();
        model.addAttribute("estimate", quote); // モデル名は estimate のまま

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
