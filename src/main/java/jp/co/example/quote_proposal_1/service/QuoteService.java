package jp.co.example.quote_proposal_1.service;

import java.util.Optional;

import jp.co.example.quote_proposal_1.entity.Quote;
import jp.co.example.quote_proposal_1.form.QuoteForm;

public interface QuoteService {
    int calculatePremium(QuoteForm quoteForm); // 現在は使用されていませんが、残しておきます
    Quote registerQuote(QuoteForm quoteForm); // ★★★ 引数をQuoteFormのみに変更 ★★★
    Optional<Quote> findById(Long id);
    Optional<Quote> findLatestQuoteByCustomerId(Long customerId);
}