package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional; // Optional を使用するため追加

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // ★追加: @PostMapping を使用するため
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ★追加: リダイレクト時のメッセージに使用するため

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.Estimate;
import jp.co.example.quote_proposal_1.service.CustomerService;
import jp.co.example.quote_proposal_1.service.EstimateService;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EstimateService estimateService;

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @GetMapping("/{customerId}/estimates")
    public String showCustomerEstimates(@PathVariable("customerId") Long customerId, Model model) {
        // CustomerService の findById を使用する場合（Optionalを返す）
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (customerOptional.isEmpty()) {
            return "redirect:/error"; // 顧客が見つからない場合、エラーページなどにリダイレクト
        }
        Customer customer = customerOptional.get();

        // または、CustomerService に `Customer findCustomerById(Long id)` がある場合
        // Customer customer = customerService.findCustomerById(customerId);
        // if (customer == null) {
        //     return "redirect:/error";
        // }

        List<Estimate> estimates = estimateService.findEstimatesByCustomerId(customerId);

        model.addAttribute("customer", customer);
        model.addAttribute("estimates", estimates);

        // リダイレクト属性にメッセージがあればモデルに追加 (削除成功/失敗メッセージ表示用)
        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
        }
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }

        return "customers/estimates";
    }

    // ★★★ ここから追加するメソッド: 見積もり削除処理 ★★★
    @PostMapping("/{customerId}/estimates/{estimateId}/delete")
    public String deleteEstimate(@PathVariable("customerId") Long customerId,
                                 @PathVariable("estimateId") Long estimateId,
                                 RedirectAttributes redirectAttributes) {
        try {
            estimateService.deleteEstimateById(estimateId);
            redirectAttributes.addFlashAttribute("message", "見積もりID: " + estimateId + " を削除しました。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "見積もりの削除に失敗しました。: " + e.getMessage());
            // エラーログを出力 (開発時デバッグ用)
            e.printStackTrace();
        }
        // 削除後、同じ顧客の見積もり一覧ページにリダイレクト
        return "redirect:/customers/" + customerId + "/estimates";
    }
    // ★★★ 追加するメソッドはここまで ★★★
}