package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jp.co.example.quote_proposal_1.entity.CancerInsuranceDetail; // 追加: がん保険詳細エンティティ
import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.entity.MedicalInsuranceDetail;
import jp.co.example.quote_proposal_1.entity.WholeLifeInsuranceDetail; // 追加: 終身保険詳細エンティティ
import jp.co.example.quote_proposal_1.service.CancerInsuranceService; // 追加: がん保険サービス
import jp.co.example.quote_proposal_1.service.MedicalInsuranceService;
import jp.co.example.quote_proposal_1.service.ProductService;
import jp.co.example.quote_proposal_1.service.WholeLifeInsuranceService; // 追加: 終身保険サービス

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final MedicalInsuranceService medicalInsuranceService;
    private final CancerInsuranceService cancerInsuranceService; 
    private final WholeLifeInsuranceService wholeLifeInsuranceService; 

    public ProductController(
            ProductService productService,
            MedicalInsuranceService medicalInsuranceService,
            CancerInsuranceService cancerInsuranceService,
            WholeLifeInsuranceService wholeLifeInsuranceService) { 
        this.productService = productService;
        this.medicalInsuranceService = medicalInsuranceService;
        this.cancerInsuranceService = cancerInsuranceService; 
        this.wholeLifeInsuranceService = wholeLifeInsuranceService; 
    }

    // 商品カテゴリ選択ページを表示する
    @GetMapping
    public String showProductCategories() {
        return "products/category_list"; 
    }

    // 医療保険詳細ページを表示する
    @GetMapping("/medical")
    public String showMedicalInsuranceDetail(Model model) {
        Long medicalProductId = 3L; 

        Optional<InsuranceProduct> productOptional = productService.findProductById(medicalProductId);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "医療保険が見つかりません");
        }
        InsuranceProduct product = productOptional.get();
        model.addAttribute("product", product);

        List<MedicalInsuranceDetail> medicalRates = medicalInsuranceService.getMedicalRatesByProductId(medicalProductId);
        model.addAttribute("medicalRates", medicalRates);

        return "products/medical_insurance";
    }

    // がん保険詳細ページを表示する
    @GetMapping("/cancer")
    public String showCancerInsuranceDetail(Model model) {
        Long cancerProductId = 1L;

        Optional<InsuranceProduct> productOptional = productService.findProductById(cancerProductId);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "がん保険が見つかりません");
        }
        InsuranceProduct product = productOptional.get();
        model.addAttribute("product", product);

        List<CancerInsuranceDetail> cancerRates = cancerInsuranceService.getCancerRatesByProductId(cancerProductId);
        model.addAttribute("cancerRates", cancerRates); 

        return "products/cancer_insurance"; 
    }

    // 終身保険詳細ページを表示する
    @GetMapping("/wholelife")
    public String showWholeLifeInsuranceDetail(Model model) {
        Long wholeLifeProductId = 2L; 

        Optional<InsuranceProduct> productOptional = productService.findProductById(wholeLifeProductId);

        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "終身保険が見つかりません");
        }
        InsuranceProduct product = productOptional.get();
        model.addAttribute("product", product);

        List<WholeLifeInsuranceDetail> wholeLifeRates = wholeLifeInsuranceService.getWholeLifeRatesByProductId(wholeLifeProductId);
        model.addAttribute("wholeLifeRates", wholeLifeRates); 

        return "products/wholelife_insurance"; 
    }
}
