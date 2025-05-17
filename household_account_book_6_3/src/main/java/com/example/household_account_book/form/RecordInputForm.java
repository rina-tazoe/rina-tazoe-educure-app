package com.example.household_account_book.form;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordInputForm {
    private LocalDate date;
    private String type;
    private Integer categoryId; 
    private String description;
    private BigDecimal amount;

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    // セッター
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}