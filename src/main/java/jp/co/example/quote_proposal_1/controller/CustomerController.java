package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.service.CustomerService;
import jp.co.example.quote_proposal_1.service.QuoteService; 

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private QuoteService quoteService; 

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @GetMapping("/{customerId}/estimates")
    public String showCustomerEstimates(@PathVariable("customerId") Long customerId, Model model) {
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (customerOptional.isEmpty()) {
            return "redirect:/error";
        }
        Customer customer = customerOptional.get();

        // QuoteService を使用し、Quote オブジェクトを取得
        List<Quote> quotes = quoteService.findQuotesByCustomerId(customerId); 

        model.addAttribute("customer", customer);
        model.addAttribute("estimates", quotes); // 変数名を estimates のままにしておく (HTMLの変更を最小限にするため)

        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }

        return "customers/estimates";
    }

    @PostMapping("/{customerId}/estimates/{estimateId}/delete")
    public String deleteEstimate(@PathVariable("customerId") Long customerId,
                                 @PathVariable("estimateId") Long estimateId,
                                 RedirectAttributes redirectAttributes) {
        try {
            // QuoteService を使用
            quoteService.deleteQuoteById(estimateId);
            redirectAttributes.addFlashAttribute("message", "見積もりID: " + estimateId + " を削除しました。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "見積もりの削除に失敗しました。: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/customers/" + customerId + "/estimates";
    }
}
