    package jp.co.example.quote_proposal_1.controller;

    import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.quote_proposal_1.entity.InsuranceProduct;
import jp.co.example.quote_proposal_1.form.QuoteForm;
import jp.co.example.quote_proposal_1.service.ProductService; 

    @Controller
    @RequestMapping("/estimates") 
    public class EstimateController { 

        private final ProductService productService;

        public EstimateController(ProductService productService) {
            this.productService = productService;
        }

        @GetMapping("/new") 
        public String showNewEstimateForm(Model model) {
            System.out.println("DEBUG: showNewEstimateForm method called."); 

            QuoteForm quoteForm = new QuoteForm();
            model.addAttribute("quoteForm", quoteForm);

            List<InsuranceProduct> products = productService.findAllProducts(); 
            model.addAttribute("products", products); 

            return "quote/form"; 
        }

    }
    