    package jp.co.example.quote_proposal_1.controller;

    import java.util.List; // 必要であれば

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping; // 必要であれば

import jp.co.example.quote_proposal_1.entity.InsuranceProduct; // ProductServiceを使うならこれも必要
import jp.co.example.quote_proposal_1.form.QuoteForm; // QuoteFormのインポートも必要
import jp.co.example.quote_proposal_1.service.ProductService; // ProductServiceを使うならこれも必要

    @Controller
    @RequestMapping("/estimates") // または特定のパス
    public class EstimateController { // 仮にEstimateControllerとします

        // ProductServiceが必要ならDIする
        private final ProductService productService;

        // コンストラクタにDI
        public EstimateController(ProductService productService) {
            this.productService = productService;
        }

        @GetMapping("/new") // ★★★ このメソッドです ★★★
        public String showNewEstimateForm(Model model) {
            System.out.println("DEBUG: showNewEstimateForm method called."); // 追加

            // ★★★ ここに QuoteForm と products を追加する ★★★
            QuoteForm quoteForm = new QuoteForm();
            model.addAttribute("quoteForm", quoteForm);

            List<InsuranceProduct> products = productService.findAllProducts(); // 必要な場合
            model.addAttribute("products", products); // 必要な場合

            return "quote/form"; // ここで quote/form を返しているはず
        }

        // ... その他のメソッド
    }
    