package jp.co.example.quote_proposal_1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.quote_proposal_1.entity.Customer;
import jp.co.example.quote_proposal_1.entity.Estimate; // Estimateも必要になる可能性
import jp.co.example.quote_proposal_1.service.CustomerService;
import jp.co.example.quote_proposal_1.service.EstimateService; // EstimateServiceをインポート

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final EstimateService estimateService; // 見積もり詳細へのリダイレクトのため追加

    @Autowired
    public CustomerController(CustomerService customerService, EstimateService estimateService) {
        this.customerService = customerService;
        this.estimateService = estimateService;
    }

    // ⑧ 顧客一覧画面の表示
    @GetMapping
    public String showCustomerList(Model model) {
        List<Customer> customers = customerService.findAllCustomers(); // 仮のメソッド
        model.addAttribute("customers", customers);
        return "customers/list"; // src/main/resources/templates/customers/list.html を表示
    }

    // 顧客詳細（実際は登録済みの見積もり結果画面⑥へ遷移）
    // 顧客の最新の見積もりを取得してリダイレクトする例
    @GetMapping("/{id}")
    public String showCustomerDetail(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Customer> customerOpt = customerService.findCustomerById(id);
        if (customerOpt.isPresent()) {
            // 顧客に関連する最新の見積もりを取得
            // ここはビジネスロジックにより変わりますが、例として最新の見積もりを取得
            Optional<Estimate> latestEstimate = estimateService.findLatestEstimateByCustomerId(id); // 仮のメソッド
            if (latestEstimate.isPresent()) {
                return "redirect:/estimates/" + latestEstimate.get().getId(); // ⑥の見積もり詳細へリダイレクト
            } else {
                redirectAttributes.addFlashAttribute("error", "この顧客には登録された見積もりが見つかりません。");
                return "redirect:/customers"; // 見積もりがない場合は顧客一覧に戻るなど
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "指定された顧客が見つかりません。");
            return "redirect:/customers";
        }
    }

    // 顧客編集（実際は見積もり編集画面⑦へ遷移）
    // 顧客の最新の見積もりを取得してリダイレクトする例
    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Customer> customerOpt = customerService.findCustomerById(id);
        if (customerOpt.isPresent()) {
            Optional<Estimate> latestEstimate = estimateService.findLatestEstimateByCustomerId(id); // 仮のメソッド
            if (latestEstimate.isPresent()) {
                return "redirect:/estimates/" + latestEstimate.get().getId() + "/edit"; // ⑦の見積もり編集へリダイレクト
            } else {
                redirectAttributes.addFlashAttribute("error", "この顧客には編集可能な見積もりがありません。");
                return "redirect:/customers/" + id; // 詳細画面に戻るなど
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "指定された顧客が見つかりません。");
            return "redirect:/customers";
        }
    }
}