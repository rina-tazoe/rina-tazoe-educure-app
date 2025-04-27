package com.example.household_account_book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.household_account_book", "com.example.security"})
public class HouseholdAccountBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseholdAccountBookApplication.class, args);
    }
}