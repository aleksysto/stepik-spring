package com.example.demo.service;

import com.example.demo.domain.Company;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {


    @Autowired
    @Qualifier("company")
    Company company;

    public Company getCompany() {
        return company;
    }
    public Optional<Person> getPersonById(int id) {
        try {
            Person res = company.getAllWorkers().get(id);
            return Optional.of(res);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
    public boolean existsByEmail(String email) {
        return company.existsByEmail(email);
    }
    public Optional<Person> findByEmail(String email) {
        return company.findByEmail(email);
    }
    public double getSalarySum(){
        return company.getSalarySum();
    }

    public Person addPerson(Person person) {
        company.getAllWorkers().add(person);
        return person;
    }

    public Optional<Person> updatePerson(int id, Person updatedPerson) {
        try {
            Person res = company.getAllWorkers().set(id, updatedPerson);
            return Optional.of(res);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public boolean deletePerson(int id) {
        try {
            company.getAllWorkers().remove(id);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public List<Person> getAllWorkers() {
        return company.getAllWorkers();
    }
    public int getNewId(){
        return company.getNewId();
    }
    public  List<String> getEmployeeCountries() {
        return company.getEmployeeCountries();
    }

    public Map<String, Double> getCurrencySalaryPairs() {
        return company.getCurrencySalaryPairs();
    }

    public List<Person> getEmployeesFromCountry(String country) {
        return company.getEmployeesFromCountry(country);
    }

}