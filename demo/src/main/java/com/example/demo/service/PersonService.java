package com.example.demo.service;

import com.example.demo.domain.Company;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            return company.findById(id);
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
    public List<Person> importFromCSV(MultipartFile file) throws IOException {
        return List.of(new Person());
    }

    public Optional<Person> updatePerson(int id, Person updatedPerson) {
        try {
            Optional<Person> toRemove = company.findById(id);
            if (toRemove.isPresent()){
                toRemove.get().setName(updatedPerson.getName());
                toRemove.get().setCompanyName(updatedPerson.getCompanyName());
                toRemove.get().setSalary(updatedPerson.getSalary());
                toRemove.get().setEmail(updatedPerson.getEmail());
                toRemove.get().setCountry(updatedPerson.getCountry());
                toRemove.get().setCurrency(updatedPerson.getCurrency());
                toRemove.get().setLastName(updatedPerson.getLastName());
                toRemove.get().setImagePath(updatedPerson.getImagePath());
                return Optional.of(toRemove.get());
            } else {
                return Optional.empty();
            }
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
    public void addPersons(List<Person> persons){
        this.company.addPersons(persons);
    }

    public boolean deletePerson(int id) {
        try {
            Optional<Person> toRemove = company.findById(id);
            if (toRemove.isPresent()) {
                company.removeById(id);
            } else
            {
                return false;
            }
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