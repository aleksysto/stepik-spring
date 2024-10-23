package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.Person;

import java.util.List;

@Configuration
public class PersonServiceConfig {

    @Bean
    public List<Person> manager(CsvService csvService) {
        return csvService.readCsvFile("MOCK_DATA.csv");
    }

}