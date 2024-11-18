package com.example.demo.validation;

import com.example.demo.service.PersonService;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Constraint(validatedBy = TotalSalaryValidator.class)
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TotalSalary {
    String message() default "Salary cannot be over company budget";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
