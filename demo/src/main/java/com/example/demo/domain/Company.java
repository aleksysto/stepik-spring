package com.example.demo.domain;

import java.util.List;
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

}
