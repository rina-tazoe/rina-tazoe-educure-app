package jp.co.example.quote_proposal_1.entity; // もしくは jp.co.example.quote_proposal_1.dto

import java.math.BigDecimal;

// 見積もり計算結果を保持するシンプルなDTO（データ転送オブジェクト）
public class Premium {
    private BigDecimal monthlyPremium;
    private BigDecimal surrenderValue;

    public Premium(BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        this.monthlyPremium = monthlyPremium;
        this.surrenderValue = surrenderValue;
    }

    // Getterメソッド
    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

    public BigDecimal getSurrenderValue() {
        return surrenderValue;
    }

    // 必要であればSetterメソッドやtoString()などを追加
}