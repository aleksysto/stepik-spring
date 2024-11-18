package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {
    private final List<String> allowedCurrencies = List.of("EUR", "USD", "GBP", "JPY");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && allowedCurrencies.contains(value);
    }
}
