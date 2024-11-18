package com.example.demo.validation;

import com.example.demo.domain.Person;
import com.example.demo.service.PersonService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TotalSalaryValidator implements ConstraintValidator<TotalSalary, Double> {
    @Autowired
    private PersonService personService;

    private final BigDecimal budget = BigDecimal.valueOf(190_000_000);

    @Override
    public boolean isValid(Double salary, ConstraintValidatorContext context) {
        BigDecimal totalSalary = BigDecimal.valueOf(personService.getSalarySum());
        BigDecimal employeeSalary = BigDecimal.valueOf(salary);
        return totalSalary.add(employeeSalary).compareTo(budget) <= 0;
    }
}
