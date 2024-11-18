package com.example.demo.validation;

import com.example.demo.domain.Person;
import com.example.demo.service.PersonService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private PersonService personService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !personService.existsByEmail(email);
    }
}
