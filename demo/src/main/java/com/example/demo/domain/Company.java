package com.example.demo.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Company {

    private Person secretary;
    private Person president;
    private Person vicepresident;
    private List<Person> workers;

    public Company(Person president, Person vicepresident, Person secretary, List<Person> workers) {
        this.president = president;
        this.vicepresident = vicepresident;
        this.secretary = secretary;
        this.workers = workers;
    }

    public void displayKeyEmployees() {
        System.out.println("President: " + president);
        System.out.println("Vice President: " + vicepresident);
        System.out.println("Secretary: " + secretary);
    }
    public void setPresident(Person president) {
        this.president = president;
    }

    public void setSecretary(Person secretary) {
        this.secretary = secretary;
    }

    public void setVicepresident(Person vicepresident) {
        this.vicepresident = vicepresident;
    }

    public void setWorkers(List<Person> workers) {
        this.workers = workers;
    }

    public Person getPresident() {
        return president;
    }

    public Person getSecretary() {
        return secretary;
    }

    public Person getVicepresident() {
        return vicepresident;
    }
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
    public Map<String, Double> getCurrencySalaryPairs() {
        Map<String, Double> currencySums = new HashMap<>();
        List<AbstractMap.SimpleEntry<String, Double>> currencySalaryPairs =
                workers.stream()
                        .map(person -> new AbstractMap.SimpleEntry<>(person.getCurrency(), person.getSalary()))
                        .collect(Collectors.toList());
        currencySalaryPairs.forEach(pair -> currencySums.put(pair.getKey(), currencySums.getOrDefault(pair.getKey(), 0.0) + pair.getValue()));
        return currencySums;
    }

    public List<Person> getEmployeesFromCountry(String country) {
        return workers.stream().
                filter(employee -> employee.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }
    public boolean existsByEmail(String email) {
        return workers.stream().anyMatch(employee -> employee.getEmail().equalsIgnoreCase(email));
    }

    public List<String> getEmployeeCountries() {
        return workers.stream().map(Person::getCountry).distinct().sorted().collect(Collectors.toList());
    }
    public double getSalarySum(){
        return workers.stream().map(Person::getSalary).reduce(0.0, Double::sum);
    }
    public void addPersons(List<Person> persons){
        this.workers.addAll(persons);
    }
    public Optional<Person> findByEmail(String email) {
        return workers.stream().filter(employee -> employee.getEmail().equalsIgnoreCase(email)).findFirst();
    }
    public int getNewId(){
        Collections.reverse(workers);
        int newId = workers.get(0).getId() + 1;
        Collections.reverse(workers);
        return newId;
    }
    public Optional<Person> findById(int id){
        return workers.stream().filter(employee -> employee.getId() == id).findFirst();
    }
    public void removeById(int id){
        workers.removeIf(employee -> employee.getId() == id);
    }



}
