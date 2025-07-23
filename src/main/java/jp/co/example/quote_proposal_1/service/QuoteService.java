package jp.co.example.quote_proposal_1.service;

import java.util.List;
import java.util.Optional;

import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.form.QuoteForm;

public interface QuoteService {
    // 仮のcalculatePremiumは削除または無視
    // int calculatePremium(QuoteForm quoteForm);

    // 見積もり計算ロジック
    QuoteForm getCalculatedPremium(QuoteForm quoteForm);

    // 見積もり登録
    Quote registerQuote(QuoteForm quoteForm);

    // IDで見積もりを検索
    Optional<Quote> findById(Long id);

    // 顧客IDで見積もり履歴を複数取得
    List<Quote> findQuotesByCustomerId(Long customerId);
    
    // 顧客IDで最新の見積もりを取得
    Optional<Quote> findLatestQuoteByCustomerId(Long customerId);

    // 見積もり削除
    void deleteQuoteById(Long id);
}
