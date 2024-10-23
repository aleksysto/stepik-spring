package com.example.demo.service;

import com.example.demo.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.Person;

import java.util.List;

@Configuration
public class PersonServiceConfig {

    @Autowired
    private Person president;

    @Autowired
    private Person vicePresident;

    @Autowired
    private Person secretary;

    @Bean
    public Company company(CsvService csvService) {
        return new Company(president, vicePresident, secretary, csvService.readCsvFile("MOCK_DATA.csv"));
    }

}