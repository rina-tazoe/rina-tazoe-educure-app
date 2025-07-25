package jp.co.example.quote_proposal_1.entity;

import java.math.BigDecimal;

public class Premium {
    private BigDecimal monthlyPremium;
    private BigDecimal surrenderValue;

    public Premium(BigDecimal monthlyPremium, BigDecimal surrenderValue) {
        this.monthlyPremium = monthlyPremium;
        this.surrenderValue = surrenderValue;
    }

    public BigDecimal getMonthlyPremium() {
        return monthlyPremium;
    }

    public BigDecimal getSurrenderValue() {
        return surrenderValue;
    }

}