package com.example.demo.service;

import com.example.demo.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {


    @Autowired
    @Qualifier("company")
    Company company;

    public Company getCompany() {
        return company;
    }
}