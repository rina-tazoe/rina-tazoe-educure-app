package com.example.household_account_book.form;

import java.time.LocalDate;

public class RecordInputForm {
    private LocalDate date;
    private String type; // "収入" または "支出"
    private Integer incomeCategoryId;
    private Integer expenseCategoryId;
    private String description;
    private Integer amount;

    // ゲッター
    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Integer getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public Integer getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAmount() {
        return amount;
    }

    // セッター
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIncomeCategoryId(Integer incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public void setExpenseCategoryId(Integer expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}