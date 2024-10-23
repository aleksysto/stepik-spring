package com.example.demo.service;

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
    private Person president;

    @Autowired
    private Person vicePresident;

    @Autowired
    private Person secretary;

    public void displayKeyEmployees() {
        System.out.println("President: " + president);
        System.out.println("Vice President: " + vicePresident);
        System.out.println("Secretary: " + secretary);
    }
    @Autowired
    @Qualifier("manager")
    List<Person> workers;

    List<Person> getWorkers(){
        return workers;
    }
    // Method to get all employees
    public List<Person> getAllWorkers() {
        return workers;
    }

    public List<Person> filterByCompany(String company) {
        return workers.stream()
                .filter(employee -> employee.getCompanyName().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }

    public List<Person> sortByLastName() {
        return workers.stream()
                .sorted((p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName()))
                .collect(Collectors.toList());
    }

    public List<Person> sortByEmail() {
        return workers.stream()
                .sorted((p1, p2) -> p1.getEmail().compareToIgnoreCase(p2.getEmail()))
                .collect(Collectors.toList());
    }

}