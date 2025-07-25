package jp.co.example.quote_proposal_1.validation;

import jakarta.validation.groups.Default;

public class ValidationGroups {
    public interface QuoteCalculation extends Default {} 
    public interface CustomerRegistration extends Default {} 
}